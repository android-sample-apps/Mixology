package com.zemingo.drinksmenu.ui.mappers

import com.zemingo.drinksmenu.domain.models.IngredientModel
import com.zemingo.drinksmenu.ui.models.IngredientFilterUiModel
import java.util.function.Function

class IngredientFilterMapperUi : Function<List<IngredientModel>, List<IngredientFilterUiModel>> {

    override fun apply(t: List<IngredientModel>): List<IngredientFilterUiModel> {
        return t.map {
            IngredientFilterUiModel(
                name = it.name
            )
        }
    }
}