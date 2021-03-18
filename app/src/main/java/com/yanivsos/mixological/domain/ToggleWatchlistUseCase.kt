package com.yanivsos.mixological.domain

import com.yanivsos.mixological.domain.models.WatchlistItemModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import timber.log.Timber

class ToggleWatchlistUseCase(
    private val getWatchlistUseCase: GetWatchlistUseCase,
    private val addToWatchlistUseCase: AddToWatchlistUseCase,
    private val removeFromWatchlistUseCase: RemoveFromWatchlistUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun toggle(watchlistItemModel: WatchlistItemModel): Boolean {
        Timber.d("addToWatchlist: called with $watchlistItemModel")
        return withContext(ioDispatcher) {
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

            !isFavorite
        }
    }
}
