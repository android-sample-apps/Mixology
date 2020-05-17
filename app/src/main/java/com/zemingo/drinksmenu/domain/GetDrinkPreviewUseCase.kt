package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.domain.models.WatchlistItemModel
import com.zemingo.drinksmenu.repo.repositories.DrinkPreviewRepository
import com.zemingo.drinksmenu.repo.repositories.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class GetDrinkPreviewUseCase(
    private val repository: DrinkPreviewRepository,
    private val watchlistRepository: WatchlistRepository
) {

    private fun Flow<List<WatchlistItemModel>>.toSet(): Flow<Set<String>> {
        return map { watchlist -> watchlist.map { it.id } }
            .map { it.toSet() }
    }

    private fun combineFavorites(
        watchlist: Set<String>,
        previews: List<DrinkPreviewModel>
    ): List<DrinkPreviewModel> {
        return previews.map {
            it.copy(isFavorite = watchlist.contains(it.id))
        }
    }

    fun getAll(): Flow<List<DrinkPreviewModel>> =
        watchlistRepository
            .getWatchlist()
            .toSet()
            .combine(repository.getAll()) { watchlist: Set<String>, previews: List<DrinkPreviewModel> ->
                combineFavorites(watchlist, previews)
            }

    fun getById(id: String) =
        watchlistRepository
            .getById(id)
            .toSet()
            .combine(repository.getByIds(listOf(id))) { watchlist: Set<String>, previews: List<DrinkPreviewModel> ->
                combineFavorites(watchlist, previews)
            }
}