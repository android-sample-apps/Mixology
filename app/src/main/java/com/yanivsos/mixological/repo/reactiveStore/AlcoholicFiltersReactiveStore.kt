package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.AlcoholicFilterModel
import com.yanivsos.mixological.repo.room.AlcoholicFilterDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AlcoholicFiltersReactiveStore(
    private val alcoholicFilterDao: AlcoholicFilterDao
): NonRemovableReactiveStore<AlcoholicFilterModel, Unit> {

    override fun storeAll(data: List<AlcoholicFilterModel>) {
        GlobalScope.launch { alcoholicFilterDao.store(data) }
    }

    override fun get(param: Unit): Flow<List<AlcoholicFilterModel>> {
        return alcoholicFilterDao.getAll()
    }
}
