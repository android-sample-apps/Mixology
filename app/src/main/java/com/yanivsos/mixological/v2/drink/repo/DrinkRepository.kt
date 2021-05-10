package com.yanivsos.mixological.v2.drink.repo

import com.yanivsos.mixological.database.DrinkModel
import com.yanivsos.mixological.database.DrinkPreviewModel
import com.yanivsos.mixological.database.WatchlistItemModel
import com.yanivsos.mixological.database.debugPrint
import com.yanivsos.mixological.network.DrinkService
import com.yanivsos.mixological.v2.drink.dao.DrinkDao
import com.yanivsos.mixological.v2.drink.mappers.toFirstOrNullModel
import com.yanivsos.mixological.v2.drink.mappers.toModel
import com.yanivsos.mixological.v2.drink.mappers.toPreviewModel
import com.yanivsos.mixological.v2.favorites.dao.FavoriteDrinksDao
import com.yanivsos.mixological.v2.favorites.utils.mergeWithFavorites
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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
            .mergeWithFavorites(favoriteDrinksDao.getAll(), defaultDispatcher)
    }

    suspend fun fetchPreviewsByLetter(char: Char): List<DrinkPreviewModel> =
        withContext(ioDispatcher) {
            drinkService
                .searchByFirstLetter(char.toString())
                .toModel()
                .toPreviewModel()
        }

    suspend fun storePreviews(previews: List<DrinkPreviewModel>) =
        withContext(ioDispatcher) {
            drinkDao.storePreviews(
                previews
                    .mergeWithFavorites(favoriteDrinksDao.getAll().first())
            )
        }

    //filtering
    suspend fun filterBy(filter: DrinkFilterRequest): List<DrinkPreviewModel> =
        withContext(ioDispatcher) {
            when (filter) {
                is DrinkFilterRequest.Alcoholic -> drinkService.filterByAlcoholic(filter.alcoholic)
                is DrinkFilterRequest.Category -> drinkService.filterByCategory(filter.category)
                is DrinkFilterRequest.Glass -> drinkService.filterByGlass(filter.glass)
                is DrinkFilterRequest.Ingredient -> drinkService.filterByIngredient(filter.ingredient)
            }.toModel()
        }
}

sealed class DrinkFilterRequest {
    data class Alcoholic(val alcoholic: String) : DrinkFilterRequest()
    data class Category(val category: String) : DrinkFilterRequest()
    data class Glass(val glass: String) : DrinkFilterRequest()
    data class Ingredient(val ingredient: String) : DrinkFilterRequest()
}

sealed class DrinkFilter(val name: String) {
    data class Alcoholic(val alcoholic: String) : DrinkFilter(alcoholic)
    data class Category(val category: String) : DrinkFilter(category)
    data class Glass(val glass: String) : DrinkFilter(glass)
    data class Ingredients(val ingredient: String) : DrinkFilter(ingredient)
}
