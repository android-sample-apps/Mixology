package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.MostPopularModel
import com.yanivsos.mixological.repo.room.MostPopularDao
import kotlinx.coroutines.flow.Flow

class MostPopularReactiveStore(
    private val mostPopularDao: MostPopularDao
) : ReplaceAllReactiveStore<String, MostPopularModel, Unit> {

    override fun storeAll(data: List<MostPopularModel>) {
        mostPopularDao.insertAll(data)
    }

    override fun get(param: Unit): Flow<List<MostPopularModel>> {
        return mostPopularDao.getAll()
    }

    override fun remove(keys: List<String>) {
    }

    override fun removeAll() {
        mostPopularDao.removeAll()
    }

    override fun replaceAll(data: List<MostPopularModel>) {
        mostPopularDao.replaceAll(data)
    }
}