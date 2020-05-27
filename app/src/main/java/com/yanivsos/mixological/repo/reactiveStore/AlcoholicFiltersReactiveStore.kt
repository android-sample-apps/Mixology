package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.AlcoholicFilterModel
import com.yanivsos.mixological.repo.room.AlcoholicFilterDao
import kotlinx.coroutines.flow.Flow

class AlcoholicFiltersReactiveStore(
    private val alcoholicFilterDao: AlcoholicFilterDao
): ReactiveStore<String, AlcoholicFilterModel, Void> {

    override fun getAll(key: List<String>?): Flow<List<AlcoholicFilterModel>> {
        return alcoholicFilterDao.getAll()
    }

    override fun getByParam(param: Void): Flow<List<AlcoholicFilterModel>> {
        TODO("Not yet implemented")
    }

    override fun storeAll(data: List<AlcoholicFilterModel>) {
        alcoholicFilterDao.storeAll(data)
    }

    override fun remove(key: String) {
        TODO("Not yet implemented")
    }
}