package com.zemingo.drinksmenu.repo.mappers

import com.zemingo.drinksmenu.models.DrinksWrapperResponse
import com.zemingo.drinksmenu.models.IngredientModel
import com.zemingo.drinksmenu.models.IngredientResponse
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