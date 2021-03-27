package com.yanivsos.mixological.v2.drink.repo

import com.yanivsos.mixological.domain.models.DrinkModel
import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.domain.models.WatchlistItemModel
import com.yanivsos.mixological.domain.models.debugPrint
import com.yanivsos.mixological.repo.DrinkService
import com.yanivsos.mixological.v2.drink.dao.DrinkDao
import com.yanivsos.mixological.v2.drink.mappers.toFirstOrNullModel
import com.yanivsos.mixological.v2.drink.mappers.toModel
import com.yanivsos.mixological.v2.favorites.dao.FavoriteDrinksDao
import com.yanivsos.mixological.v2.landingPage.repo.mergeWithFavorites
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext
import timber.log.Timber

class DrinkRepository(
    private val drinkService: DrinkService,
    private val drinkDao: DrinkDao,
    private val favoriteDrinksDao: FavoriteDrinksDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    //Drinks
    suspend fun fetchById(id: String): DrinkModel? {
        return withContext(ioDispatcher) {
            Timber.d("fetchById: id[$id]")
            drinkService
                .getDrinkById(id)
                .toFirstOrNullModel()
        }
    }

    suspend fun fetchByName(name: String): List<DrinkModel> {
        return withContext(ioDispatcher) {
            drinkService
                .searchByName(name)
                .toModel()
        }
    }

    suspend fun store(drinkModel: DrinkModel) {
        withContext(ioDispatcher) {
            Timber.d("store: id[${drinkModel.debugPrint()}]")
            drinkDao.store(drinkModel)
        }
    }

    fun getById(id: String): Flow<DrinkModel?> {
        return drinkDao
            .getById(id)
            .combine(favoriteDrinksDao.getById(id)) { drink, watchlist ->
                drink?.copy(isFavorite = watchlist != null)
            }.distinctUntilChanged()
    }

    //Favorites
    suspend fun addToFavorites(watchlistItemModel: WatchlistItemModel) {
        withContext(ioDispatcher) {
            Timber.d("addToFavorites: id[${watchlistItemModel.id}]")
            favoriteDrinksDao.store(watchlistItemModel)
        }
    }

    suspend fun removeFromFavorites(watchlistItemModel: WatchlistItemModel) {
        withContext(ioDispatcher) {
            Timber.d("removeFromFavorites: id[${watchlistItemModel.id}]")
            favoriteDrinksDao.remove(watchlistItemModel.id)
        }
    }

    fun getFavorites(): Flow<List<DrinkPreviewModel>> {
        return favoriteDrinksDao.getFavoritePreviews()
    }

    fun getFavoriteById(watchlistItemModel: WatchlistItemModel): Flow<WatchlistItemModel?> {
        return favoriteDrinksDao
            .getById(watchlistItemModel.id)
            .distinctUntilChanged()
    }

    //Previews
    fun getAllPreviews(): Flow<List<DrinkPreviewModel>> {
        return drinkDao
            .getPreviews()
            .mergeWithFavorites(favoriteDrinksDao, defaultDispatcher)
    }

    //filtering
    suspend fun filterBy(filter: DrinkFilter): List<DrinkPreviewModel> {
        return when (filter) {
            is DrinkFilter.Alcoholic -> drinkService.filterByAlcoholic(filter.alcoholic)
            is DrinkFilter.Category -> drinkService.filterByCategory(filter.category)
            is DrinkFilter.Glass -> drinkService.filterByGlass(filter.glass)
            is DrinkFilter.Ingredients -> drinkService.filterByIngredient(filter.ingredients.toQueryParams())
        }.toModel()
    }

    private suspend fun List<String>.toQueryParams(): String {
        return withContext(defaultDispatcher) {
            joinToString { it.replace(' ', '_') }
        }
    }
}

sealed class DrinkFilter {
    data class Alcoholic(val alcoholic: String) : DrinkFilter()
    data class Category(val category: String) : DrinkFilter()
    data class Glass(val glass: String) : DrinkFilter()
    data class Ingredients(val ingredients: List<String>) : DrinkFilter()
}
