package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.GlassModel
import com.yanivsos.mixological.repo.room.GlassDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GlassReactiveStore(
    private val glassDao: GlassDao
): NonRemovableReactiveStore<GlassModel, Unit> {

    override fun storeAll(data: List<GlassModel>) {
       GlobalScope.launch { glassDao.store(data) }
    }

    override fun get(param: Unit): Flow<List<GlassModel>> {
        return glassDao.getAll()
    }
}
