package com.yanivsos.mixological.v2.ingredients.repository

import com.yanivsos.mixological.domain.models.IngredientDetailsModel
import com.yanivsos.mixological.repo.DrinkService
import com.yanivsos.mixological.v2.ingredients.dao.IngredientDetailsDao
import com.yanivsos.mixological.v2.ingredients.mappers.toModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IngredientsRepository(
    private val drinkService: DrinkService,
    private val ingredientDetailsDao: IngredientDetailsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun fetchIngredient(name: String): IngredientDetailsModel {
        return withContext(ioDispatcher) {
            drinkService.getIngredientByName(name).toModel()
        }
    }

    suspend fun store(ingredientDetailsModel: IngredientDetailsModel) {
        withContext(ioDispatcher) {
            ingredientDetailsDao.store(ingredientDetailsModel)
        }
    }

    fun getByName(name: String) = ingredientDetailsDao.getByName(name)
}
