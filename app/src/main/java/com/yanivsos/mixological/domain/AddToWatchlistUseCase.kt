package com.yanivsos.mixological.domain

import com.yanivsos.mixological.domain.models.WatchlistItemModel
import com.yanivsos.mixological.in_app_review.InAppReviewRepository
import com.yanivsos.mixological.repo.repositories.WatchlistRepository
import timber.log.Timber

class AddToWatchlistUseCase(
    private val fetchAndStoreDrinkUseCase: FetchAndStoreDrinkUseCase,
    private val repository: WatchlistRepository,
    private val inAppReviewRepository: InAppReviewRepository,
) {

    suspend fun addToWatchlist(watchlistItemModel: WatchlistItemModel) {
        Timber.d("addToWatchlist: called with $watchlistItemModel")
        fetchAndStore(watchlistItemModel)
        repository.store(watchlistItemModel)
        inAppReviewRepository.onFavoriteAdded()
    }

    private suspend fun fetchAndStore(watchlistItemModel: WatchlistItemModel) {
        try {
            fetchAndStoreDrinkUseCase.fetchAndStore(watchlistItemModel.id)
        } catch (e: Exception) {
            Timber.e(e, "Unable to fetch and store drink[${watchlistItemModel.id}]")
        }
    }
}
