package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.GlassModel
import com.yanivsos.mixological.repo.room.GlassDao
import kotlinx.coroutines.flow.Flow

class GlassReactiveStore(
    private val glassDao: GlassDao
): NonRemovableReactiveStore<GlassModel, Unit> {

    override fun storeAll(data: List<GlassModel>) {
        glassDao.storeAll(data)
    }

    override fun get(param: Unit): Flow<List<GlassModel>> {
        return glassDao.getAll()
    }
}