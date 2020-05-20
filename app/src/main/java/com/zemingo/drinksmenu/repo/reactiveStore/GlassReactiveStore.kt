package com.zemingo.drinksmenu.repo.reactiveStore

import com.zemingo.drinksmenu.domain.models.GlassModel
import com.zemingo.drinksmenu.repo.room.GlassDao
import kotlinx.coroutines.flow.Flow

class GlassReactiveStore(
    private val glassDao: GlassDao
): ReactiveStore<String, GlassModel, Void> {

    override fun getAll(key: List<String>?): Flow<List<GlassModel>> {
        return glassDao.getAll()
    }

    override fun getByParam(param: Void): Flow<List<GlassModel>> {
        TODO("Not yet implemented")
    }

    override fun storeAll(data: List<GlassModel>) {
        glassDao.storeAll(data)
    }

    override fun remove(key: String) {
        TODO("Not yet implemented")
    }
}