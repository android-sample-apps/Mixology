package com.zemingo.drinksmenu.repo.repositories

import com.zemingo.drinksmenu.domain.models.WatchlistItemModel
import com.zemingo.drinksmenu.repo.reactive_store.WatchlistParam
import com.zemingo.drinksmenu.repo.reactive_store.WatchlistReactiveStore
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
}