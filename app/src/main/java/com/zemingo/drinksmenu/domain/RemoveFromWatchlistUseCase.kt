package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.WatchlistItemModel
import com.zemingo.drinksmenu.repo.repositories.WatchlistRepository
import timber.log.Timber

class RemoveFromWatchlistUseCase(
    private val repository: WatchlistRepository
) {
    fun remove(watchlistItemModel: WatchlistItemModel) {
        Timber.d("remove: [$watchlistItemModel]")
        repository.remove(watchlistItemModel.id)
    }
}