package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.repo.repositories.WatchlistRepository
import timber.log.Timber

class RemoveFromWatchlistUseCase(
    private val repository: WatchlistRepository
) {
    fun remove(id: String) {
        Timber.d("remove: [$id]")
        repository.remove(id)
    }
}