package com.yanivsos.mixological.repo.repositories

import com.yanivsos.mixological.domain.models.WatchlistItemModel
import com.yanivsos.mixological.repo.reactiveStore.WatchlistParam
import com.yanivsos.mixological.repo.reactiveStore.WatchlistReactiveStore
import kotlinx.coroutines.flow.Flow

class WatchlistRepository(
    private val watchlistReactiveStore: WatchlistReactiveStore
) {
    fun getWatchlist(): Flow<List<WatchlistItemModel>> {
        return watchlistReactiveStore.getAll()
    }

    fun getById(id: String): Flow<List<WatchlistItemModel>> {
        return watchlistReactiveStore.getByParam(WatchlistParam.ById(id))
    }

    fun storeAll(watchlist: List<WatchlistItemModel>) {
        watchlistReactiveStore.storeAll(watchlist)
    }

    fun store(watchlistItemModel: WatchlistItemModel) {
        watchlistReactiveStore.storeAll(listOf(watchlistItemModel))
    }

    fun remove(id: String) {
        watchlistReactiveStore.remove(id)
    }
}