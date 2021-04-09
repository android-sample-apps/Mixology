package com.yanivsos.mixological.v2.favorites.useCases

import com.yanivsos.mixological.database.WatchlistItemModel
import com.yanivsos.mixological.v2.drink.repo.DrinkRepository
import com.yanivsos.mixological.v2.drink.useCases.FetchAndStoreDrinkUseCase
import com.yanivsos.mixological.v2.inAppReview.InAppReviewRepository
import timber.log.Timber

class AddToFavoritesUseCase(
    private val repository: DrinkRepository,
    private val inAppReviewRepository: InAppReviewRepository,
    private val fetchAndStoreDrinkUseCase: FetchAndStoreDrinkUseCase
) {
    suspend fun addToFavorite(watchlistItemModel: WatchlistItemModel) {
        Timber.d("addToFavorite: $watchlistItemModel")
        repository.addToFavorites(watchlistItemModel)
        fetchAndStore(watchlistItemModel)
        updateInAppReview()
    }

    private suspend fun fetchAndStore(watchlistItemModel: WatchlistItemModel) {
        runCatching {
            fetchAndStoreDrinkUseCase
                .fetchAndStore(watchlistItemModel.id)
        }.onFailure {
            Timber.e(it, "Failed fetching & storing id[${watchlistItemModel.id}]")
        }
    }

    private suspend fun updateInAppReview() {
        inAppReviewRepository.incrementInAppReviewCounter()
    }
}
