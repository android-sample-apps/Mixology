package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.RecentlyViewedModel
import com.yanivsos.mixological.repo.room.RecentlyViewedDao
import kotlinx.coroutines.flow.Flow

class RecentlyViewedReactiveStore(
    private val recentlyViewedDao: RecentlyViewedDao
) : ReactiveStore<String, RecentlyViewedModel, Void> {

    override fun getAll(key: List<String>?): Flow<List<RecentlyViewedModel>> {
        return key?.let { recentlyViewedDao.getAll(it) } ?: recentlyViewedDao.getAll()
    }

    override fun storeAll(data: List<RecentlyViewedModel>) {
        recentlyViewedDao.insertAll(data)
    }

    override fun getByParam(param: Void): Flow<List<RecentlyViewedModel>> {
        TODO("Not yet implemented")
    }

    override fun remove(key: String) {
        TODO("Not yet implemented")
    }
}
