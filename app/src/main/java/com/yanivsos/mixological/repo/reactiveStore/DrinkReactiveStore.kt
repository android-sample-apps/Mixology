package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.DrinkModel
import com.yanivsos.mixological.repo.room.DrinkDao
import kotlinx.coroutines.flow.Flow

class DrinkReactiveStore(
    private val drinksDao: DrinkDao
) : ReactiveStore<String, DrinkModel, DrinkParams> {

    override fun getAll(key: List<String>?): Flow<List<DrinkModel>> {
        return drinksDao.getAll()
    }

    override fun getByParam(param: DrinkParams): Flow<List<DrinkModel>> {
        return when(param) {
            is DrinkParams.ById -> drinksDao.getById(param.id)
        }
    }

    override fun storeAll(data: List<DrinkModel>) {
        drinksDao.storeAll(data)
    }

    override fun remove(key: String) {
        drinksDao.remove(key)
    }
}

sealed class DrinkParams {
    data class ById(val id: String) : DrinkParams()
}