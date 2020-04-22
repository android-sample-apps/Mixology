package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.repo.repositories.IngredientRepository

class FetchIngredientsUseCase(
    private val repository: IngredientRepository
) {

    suspend fun fetch() {
        repository.fetchIngredients()
    }
}