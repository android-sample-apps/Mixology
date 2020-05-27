package com.yanivsos.mixological.domain

import com.yanivsos.mixological.repo.repositories.IngredientRepository

class FetchIngredientsUseCase(
    private val repository: IngredientRepository
) {

    suspend fun fetch() {
        repository.fetchIngredients()
    }
}