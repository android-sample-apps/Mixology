package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.RecentlyViewedModel
import com.yanivsos.mixological.repo.room.RecentlyViewedDao
import kotlinx.coroutines.flow.Flow

class RecentlyViewedReactiveStore(
    private val recentlyViewedDao: RecentlyViewedDao
) : NonRemovableReactiveStore<RecentlyViewedModel, RecentlyViewedParams> {

    override fun storeAll(data: List<RecentlyViewedModel>) {
        recentlyViewedDao.insertAll(data)
    }

    override fun get(param: RecentlyViewedParams): Flow<List<RecentlyViewedModel>> {
        return when (param) {
            is RecentlyViewedParams.All -> recentlyViewedDao.getAll()
        }
    }
}

sealed class RecentlyViewedParams {
    object All : RecentlyViewedParams()
}