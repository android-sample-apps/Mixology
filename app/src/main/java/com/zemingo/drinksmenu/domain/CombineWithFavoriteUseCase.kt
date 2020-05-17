package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.domain.models.WatchlistItemModel
import com.zemingo.drinksmenu.repo.repositories.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class CombineWithFavoriteUseCase(
    private val watchlistRepository: WatchlistRepository
) {
    fun combine(previewsFlow: Flow<List<DrinkPreviewModel>>): Flow<List<DrinkPreviewModel>> {
        return watchlistRepository
            .getWatchlist()
            .toSet()
            .combine(previewsFlow) { watchlist: Set<String>, previews: List<DrinkPreviewModel> ->
                combineFavorites(watchlist, previews)
            }
            .distinctUntilChanged()
    }

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
}