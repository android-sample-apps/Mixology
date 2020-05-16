package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.WatchlistItemModel
import com.zemingo.drinksmenu.repo.repositories.WatchlistRepository
import timber.log.Timber

class AddToWatchlistUseCase(
    private val repository: WatchlistRepository
) {

    suspend fun addToWatchlist(watchlistItemModel: WatchlistItemModel) {
        Timber.d("addToWatchlist: called with $watchlistItemModel")
        repository.store(watchlistItemModel)
    }
}