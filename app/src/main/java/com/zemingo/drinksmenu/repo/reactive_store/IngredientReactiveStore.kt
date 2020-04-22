package com.zemingo.drinksmenu.repo.reactive_store

import com.zemingo.drinksmenu.models.IngredientModel
import com.zemingo.drinksmenu.room.IngredientDao
import kotlinx.coroutines.flow.Flow

class IngredientReactiveStore(
    private val ingredientDao: IngredientDao
) : ReactiveStore<IngredientModel> {

    override fun getAll(): Flow<List<IngredientModel>> {
        return ingredientDao.getAll()
    }

    override fun storeAll(data: List<IngredientModel>) {
        ingredientDao.storeAll(data)
    }
}