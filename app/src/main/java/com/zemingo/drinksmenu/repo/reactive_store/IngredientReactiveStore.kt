package com.zemingo.drinksmenu.repo.reactive_store

import com.zemingo.drinksmenu.domain.models.IngredientModel
import com.zemingo.drinksmenu.repo.room.IngredientDao
import kotlinx.coroutines.flow.Flow

class IngredientReactiveStore(
    private val ingredientDao: IngredientDao
) : ReactiveStore<String, IngredientModel, Void> {

    override fun storeAll(data: List<IngredientModel>) {
        ingredientDao.storeAll(data)
    }

    override fun getAll(key: List<String>?): Flow<List<IngredientModel>> {
        return ingredientDao.getAll()
    }

    override fun getByParam(param: Void): Flow<List<IngredientModel>> {
        TODO("Not yet implemented")
    }

    override fun remove(key: String) {
        TODO("Not yet implemented")
    }
}