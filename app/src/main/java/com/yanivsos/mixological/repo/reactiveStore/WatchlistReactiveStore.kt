package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.WatchlistItemModel
import com.yanivsos.mixological.repo.room.WatchlistDao
import kotlinx.coroutines.flow.Flow

class WatchlistReactiveStore(
    private val watchlistDao: WatchlistDao
) : RemovableReactiveStore<String, WatchlistItemModel, WatchlistParam> {

    override fun get(param: WatchlistParam): Flow<List<WatchlistItemModel>> {
        return when (param) {
            WatchlistParam.All -> watchlistDao.getAll()
            is WatchlistParam.ById -> watchlistDao.getById(param.id)
        }
    }

    override fun storeAll(data: List<WatchlistItemModel>) {
        watchlistDao.storeAll(data)
    }

    override fun remove(keys: List<String>) {
        watchlistDao.remove(keys)
    }
}

sealed class WatchlistParam {
    object All : WatchlistParam()
    data class ById(val id: String) : WatchlistParam()
}