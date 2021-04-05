package com.yanivsos.mixological.v2.favorites.useCases

import com.yanivsos.mixological.database.WatchlistItemModel
import com.yanivsos.mixological.v2.drink.repo.DrinkRepository
import timber.log.Timber

class RemoveFromFavoritesUseCase(
    private val drinkRepository: DrinkRepository
) {
    suspend fun removeFromFavorites(watchlistItemModel: WatchlistItemModel) {
        Timber.d("removeFromFavorites: $watchlistItemModel")
        drinkRepository.removeFromFavorites(watchlistItemModel)
    }
}
