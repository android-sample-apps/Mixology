package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.LatestArrivalsModel
import com.yanivsos.mixological.repo.room.LatestArrivalsDao
import kotlinx.coroutines.flow.Flow

class LatestArrivalsReactiveStore(
    private val latestArrivalsDao: LatestArrivalsDao
) : ReplaceAllReactiveStore<LatestArrivalsModel, Unit> {

    override fun storeAll(data: List<LatestArrivalsModel>) {
        latestArrivalsDao.insertAll(data)
    }

    override fun get(param: Unit): Flow<List<LatestArrivalsModel>> {
        return latestArrivalsDao.getAll()
    }

    override fun replaceAll(data: List<LatestArrivalsModel>) {
        latestArrivalsDao.replaceAll(data)
    }
}