package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.DrinkModel
import com.yanivsos.mixological.repo.room.DrinkDao
import kotlinx.coroutines.flow.Flow

class DrinkReactiveStore(
    private val drinksDao: DrinkDao
) : NonRemovableReactiveStore<DrinkModel, DrinkParams> {

    override fun storeAll(data: List<DrinkModel>) {
        drinksDao.storeAll(data)
    }

    override fun get(param: DrinkParams): Flow<List<DrinkModel>> {
        return when (param) {
            is DrinkParams.All -> drinksDao.getAll()
            is DrinkParams.ById -> drinksDao.getById(param.id)
        }
    }
}

sealed class DrinkParams {
    object All : DrinkParams()
    data class ById(val id: String) : DrinkParams()
}