package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.WatchlistItemModel
import com.yanivsos.mixological.repo.room.WatchlistDao
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

    override fun remove(key: String) {
        watchlistDao.remove(key)
    }
}

sealed class WatchlistParam {
    data class ById(val id: String) : WatchlistParam()
}