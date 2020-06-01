package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.LatestArrivalsModel
import com.yanivsos.mixological.repo.room.LatestArrivalsDao
import kotlinx.coroutines.flow.Flow

class LatestArrivalsReactiveStore(
    private val latestArrivalsDao: LatestArrivalsDao
) : RemoveAllReactiveStore<String, LatestArrivalsModel, Unit> {

    override fun storeAll(data: List<LatestArrivalsModel>) {
        latestArrivalsDao.insertAll(data)
    }

    override fun get(param: Unit): Flow<List<LatestArrivalsModel>> {
        return latestArrivalsDao.getAll()
    }

    override fun remove(keys: List<String>) {
        latestArrivalsDao.remove(keys)
    }

    override fun removeAll() {
        latestArrivalsDao.removeAll()
    }
}