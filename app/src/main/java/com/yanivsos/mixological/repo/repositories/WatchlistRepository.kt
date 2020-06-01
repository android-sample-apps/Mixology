package com.yanivsos.mixological.repo.repositories

import com.yanivsos.mixological.domain.models.WatchlistItemModel
import com.yanivsos.mixological.repo.reactiveStore.RemovableReactiveStore
import com.yanivsos.mixological.repo.reactiveStore.WatchlistParam
import kotlinx.coroutines.flow.Flow

class WatchlistRepository(
    private val reactiveStore: RemovableReactiveStore<String, WatchlistItemModel, WatchlistParam>
) {
    fun getWatchlist(): Flow<List<WatchlistItemModel>> {
        return reactiveStore.get(WatchlistParam.All)
    }

    fun getById(id: String): Flow<List<WatchlistItemModel>> {
        return reactiveStore.get(WatchlistParam.ById(id))
    }

    fun storeAll(watchlist: List<WatchlistItemModel>) {
        reactiveStore.storeAll(watchlist)
    }

    fun store(watchlistItemModel: WatchlistItemModel) {
        reactiveStore.storeAll(listOf(watchlistItemModel))
    }

    fun remove(id: String) {
        reactiveStore.remove(listOf(id))
    }
}