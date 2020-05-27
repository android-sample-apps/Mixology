package com.yanivsos.mixological.repo.mappers

import com.yanivsos.mixological.repo.models.DrinksWrapperResponse
import com.yanivsos.mixological.domain.models.IngredientModel
import com.yanivsos.mixological.repo.models.IngredientResponse
import java.util.function.Function

class IngredientMapper : Function<DrinksWrapperResponse<IngredientResponse>, List<IngredientModel>> {

    override fun apply(t: DrinksWrapperResponse<IngredientResponse>): List<IngredientModel> {
        return t.data.map {
            IngredientModel(
                name = it.name
            )
        }
    }
}