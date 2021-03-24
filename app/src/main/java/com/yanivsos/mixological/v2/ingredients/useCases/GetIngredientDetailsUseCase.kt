package com.yanivsos.mixological.v2.ingredients.useCases

import com.yanivsos.mixological.domain.models.IngredientDetailsModel
import com.yanivsos.mixological.v2.ingredients.repository.IngredientDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetIngredientDetailsUseCase(
    private val repository: IngredientDetailsRepository
) {

    fun getByName(name: String): Flow<IngredientDetailsModel?> {
        return repository.getByName(name).map { it.firstOrNull() }
    }
}
