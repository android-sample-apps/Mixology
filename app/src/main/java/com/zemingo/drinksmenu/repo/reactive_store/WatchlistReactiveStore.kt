package com.zemingo.drinksmenu.repo.reactive_store

import com.zemingo.drinksmenu.domain.models.WatchlistItemModel
import com.zemingo.drinksmenu.repo.room.WatchlistDao
import kotlinx.coroutines.flow.Flow

class WatchlistReactiveStore(
    private val watchlistDao: WatchlistDao
) : ReactiveStore<String, WatchlistItemModel, WatchlistParam> {

    override fun getAll(key: List<String>?): Flow<List<WatchlistItemModel>> {
        return watchlistDao.getAll()
    }

    override fun getByParam(param: WatchlistParam): Flow<List<WatchlistItemModel>> {
        return when (param) {
            is WatchlistParam.ById -> watchlistDao.getById(param.id)
        }
    }

    override fun storeAll(data: List<WatchlistItemModel>) {
        watchlistDao.storeAll(data)
    }
}

sealed class WatchlistParam {
    data class ById(val id: String) : WatchlistParam()
}