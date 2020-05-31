package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.MostPopularModel
import com.yanivsos.mixological.repo.room.MostPopularDao
import kotlinx.coroutines.flow.Flow

class MostPopularReactiveStore(
    private val mostPopularDao: MostPopularDao
) : ReactiveStore<String, MostPopularModel, Unit> {

    override fun getAll(key: List<String>?): Flow<List<MostPopularModel>> {
        return mostPopularDao.getAll()
    }

    override fun getByParam(param: Unit): Flow<List<MostPopularModel>> {
        TODO("Not yet implemented")
    }

    override fun storeAll(data: List<MostPopularModel>) {
        mostPopularDao.insertAll(data)
    }

    override fun remove(key: String) {
        TODO("Not yet implemented")
    }

    fun removeAll() {
        mostPopularDao.removeAll()
    }
}