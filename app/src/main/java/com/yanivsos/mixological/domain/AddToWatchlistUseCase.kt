package com.yanivsos.mixological.domain

import com.yanivsos.mixological.domain.models.WatchlistItemModel
import com.yanivsos.mixological.repo.repositories.WatchlistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class AddToWatchlistUseCase(
    private val fetchAndStoreDrinkUseCase: FetchAndStoreDrinkUseCase,
    private val repository: WatchlistRepository
) {

    suspend fun addToWatchlist(watchlistItemModel: WatchlistItemModel) {
        Timber.d("addToWatchlist: called with $watchlistItemModel")
        GlobalScope.launch(Dispatchers.IO) {
            fetchAndStore(watchlistItemModel)
        }
        repository.store(watchlistItemModel)
    }

    private suspend fun fetchAndStore(watchlistItemModel: WatchlistItemModel) {
        try {
            fetchAndStoreDrinkUseCase.fetchAndStore(watchlistItemModel.id)
        } catch (e: Exception) {
            Timber.e(e, "Unable to fetch and store drink[${watchlistItemModel.id}]")
        }
    }
}