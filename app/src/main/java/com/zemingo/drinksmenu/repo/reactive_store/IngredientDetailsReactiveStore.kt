package com.zemingo.drinksmenu.repo.reactive_store

import com.zemingo.drinksmenu.domain.models.IngredientDetailsModel
import com.zemingo.drinksmenu.repo.room.IngredientDetailsDao
import kotlinx.coroutines.flow.Flow

class IngredientDetailsReactiveStore(
    private val ingredientDetailsDao: IngredientDetailsDao
) : ReactiveStore<String, IngredientDetailsModel, IngredientDetailsParam> {

    override fun getAll(key: List<String>?): Flow<List<IngredientDetailsModel>> {
        return ingredientDetailsDao.getAll()
    }

    override fun getByParam(param: IngredientDetailsParam): Flow<List<IngredientDetailsModel>> {
        return when (param) {
            is IngredientDetailsParam.GetByName -> ingredientDetailsDao.getByName(param.name)
        }
    }

    override fun storeAll(data: List<IngredientDetailsModel>) {
        ingredientDetailsDao.storeAll(data)
    }
}

sealed class IngredientDetailsParam {
    data class GetByName(val name: String) : IngredientDetailsParam()
}