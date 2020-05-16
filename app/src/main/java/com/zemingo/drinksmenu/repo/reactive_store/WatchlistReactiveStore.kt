package com.zemingo.drinksmenu.repo.reactive_store

import com.zemingo.drinksmenu.domain.models.WatchlistItemModel
import com.zemingo.drinksmenu.repo.room.WatchlistDao
import kotlinx.coroutines.flow.Flow

class WatchlistReactiveStore(
    private val watchlistDao: WatchlistDao
): ReactiveStore<String, WatchlistItemModel, Void> {

    override fun getAll(key: List<String>?): Flow<List<WatchlistItemModel>> {
        return watchlistDao.getAll()
    }

    override fun getByParam(param: Void): Flow<List<WatchlistItemModel>> {
        TODO("Not yet implemented")
    }

    override fun storeAll(data: List<WatchlistItemModel>) {
        watchlistDao.storeAll(data)
    }
}