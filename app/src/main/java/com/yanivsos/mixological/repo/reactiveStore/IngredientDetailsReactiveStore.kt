package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.IngredientDetailsModel
import com.yanivsos.mixological.repo.room.IngredientDetailsDao
import kotlinx.coroutines.flow.Flow

class IngredientDetailsReactiveStore(
    private val ingredientDetailsDao: IngredientDetailsDao
) : NonRemovableReactiveStore<IngredientDetailsModel, IngredientDetailsParam> {

    override fun storeAll(data: List<IngredientDetailsModel>) {
        ingredientDetailsDao.storeAll(data)
    }

    override fun get(param: IngredientDetailsParam): Flow<List<IngredientDetailsModel>> {
        return when (param) {
            is IngredientDetailsParam.GetByName -> ingredientDetailsDao.getByName(param.name)
            is IngredientDetailsParam.All -> ingredientDetailsDao.getAll()
        }
    }
}

sealed class IngredientDetailsParam {
    object All : IngredientDetailsParam()
    data class GetByName(val name: String) : IngredientDetailsParam()
}