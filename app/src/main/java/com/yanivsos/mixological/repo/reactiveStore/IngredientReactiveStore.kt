package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.IngredientModel
import com.yanivsos.mixological.repo.room.IngredientDao
import kotlinx.coroutines.flow.Flow

class IngredientReactiveStore(
    private val ingredientDao: IngredientDao
) : NonRemovableReactiveStore<IngredientModel, IngredientParams> {

    override fun storeAll(data: List<IngredientModel>) {
        ingredientDao.storeAll(data)
    }

    override fun get(param: IngredientParams): Flow<List<IngredientModel>> {
        return when (param) {
            is IngredientParams.All -> ingredientDao.getAll()
            is IngredientParams.ByName -> ingredientDao.getByName(param.name)
        }
    }
}

sealed class IngredientParams {
    object All : IngredientParams()
    data class ByName(val name: String) : IngredientParams()
}