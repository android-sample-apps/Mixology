package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.LatestArrivalsModel
import com.yanivsos.mixological.repo.room.LatestArrivalsDao
import kotlinx.coroutines.flow.Flow

class LatestArrivalsReactiveStore(
    private val latestArrivalsDao: LatestArrivalsDao
): ReactiveStore<String, LatestArrivalsModel, Unit> {

    override fun getAll(key: List<String>?): Flow<List<LatestArrivalsModel>> {
        return latestArrivalsDao.getAll()
    }

    override fun getByParam(param: Unit): Flow<List<LatestArrivalsModel>> {
        TODO("Not yet implemented")
    }

    override fun storeAll(data: List<LatestArrivalsModel>) {
        latestArrivalsDao.insertAll(data)
    }

    override fun remove(key: String) {
        TODO("Not yet implemented")
    }

    fun removeAll() {
        latestArrivalsDao.removeAll()
    }
}