package com.yanivsos.mixological.v2.drink

import com.yanivsos.mixological.domain.models.DrinkModel
import com.yanivsos.mixological.repo.DrinkService
import com.yanivsos.mixological.v2.drink.dao.DrinkDao
import com.yanivsos.mixological.v2.drink.mappers.toFirstOrNullModel
import kotlinx.coroutines.flow.Flow

class DrinkRepository(
    private val drinkService: DrinkService,
    private val drinkDao: DrinkDao,
) {
    suspend fun fetchById(id: String): DrinkModel? {
        return drinkService
            .getDrinkById(id)
            .toFirstOrNullModel()
    }

    fun getById(id: String): Flow<DrinkModel> {
        return drinkDao.getById(id)
    }

    suspend fun store(drinkModel: DrinkModel) {
        drinkDao.store(drinkModel)
    }
}
