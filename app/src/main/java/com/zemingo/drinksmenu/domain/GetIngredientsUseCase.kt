package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.IngredientModel
import com.zemingo.drinksmenu.repo.repositories.IngredientRepository
import kotlinx.coroutines.flow.Flow

class GetIngredientsUseCase(
    private val repository: IngredientRepository
) {

    fun getAll(): Flow<List<IngredientModel>> {
        return repository.getAll()
    }
}