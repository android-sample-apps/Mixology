package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.WatchlistItemModel
import kotlinx.coroutines.flow.*
import timber.log.Timber

class ToggleWatchlistUseCase(
    private val getWatchlistUseCase: GetWatchlistUseCase,
    private val addToWatchlistUseCase: AddToWatchlistUseCase,
    private val removeFromWatchlistUseCase: RemoveFromWatchlistUseCase
) {
    suspend fun toggle(watchlistItemModel: WatchlistItemModel) {
        Timber.d("addToWatchlist: called with $watchlistItemModel")
        val isFavorite = getWatchlistUseCase
            .getById(watchlistItemModel.id)
            .map { it != null }
            .first()
        Timber.d("isFavorite = $isFavorite")
        if (isFavorite) {
            removeFromWatchlistUseCase.remove(watchlistItemModel)
        } else {
            addToWatchlistUseCase.addToWatchlist(watchlistItemModel)
        }


    }
}