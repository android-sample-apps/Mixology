package com.yanivsos.mixological.domain

import com.yanivsos.mixological.domain.models.WatchlistItemModel
import com.yanivsos.mixological.repo.repositories.WatchlistRepository
import timber.log.Timber

class RemoveFromWatchlistUseCase(
    private val repository: WatchlistRepository
) {
    fun remove(watchlistItemModel: WatchlistItemModel) {
        Timber.d("remove: [$watchlistItemModel]")
        repository.remove(watchlistItemModel.id)
    }
}