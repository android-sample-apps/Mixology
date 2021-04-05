package com.yanivsos.mixological.v2.drink.useCases

import com.yanivsos.mixological.database.DrinkModel
import com.yanivsos.mixological.v2.drink.repo.DrinkRepository
import com.yanivsos.mixological.v2.ingredients.useCases.FetchAndStoreIngredientUseCase

class FetchAndStoreDrinkUseCase(
    private val repo: DrinkRepository,
    private val fetchAndStoreIngredientUseCase: FetchAndStoreIngredientUseCase
) {
    suspend fun fetchAndStore(drinkId: String) {
        val drink = repo.fetchById(drinkId)
        requireNotNull(drink)
        storeDrink(drink)
        fetchAndStoreIngredients(drink)
    }

    private suspend fun storeDrink(drink: DrinkModel) {
        repo.store(drink)
    }

    private suspend fun fetchAndStoreIngredients(drink: DrinkModel) {
        fetchAndStoreIngredientUseCase.fetchAndStore(*drink.ingredients.keys.toTypedArray())
    }
}
