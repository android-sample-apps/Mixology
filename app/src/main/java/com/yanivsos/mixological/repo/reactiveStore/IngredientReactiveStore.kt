package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.IngredientModel
import com.yanivsos.mixological.repo.room.IngredientDao
import kotlinx.coroutines.flow.Flow

class IngredientReactiveStore(
    private val ingredientDao: IngredientDao
) : NonRemovableReactiveStore<IngredientModel, Unit> {

    override fun storeAll(data: List<IngredientModel>) {
        ingredientDao.storeAll(data)
    }

    override fun get(param: Unit): Flow<List<IngredientModel>> {
        return ingredientDao.getAll()
    }
}