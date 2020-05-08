package com.zemingo.drinksmenu.ui.mappers

import com.zemingo.drinksmenu.domain.models.IngredientDetailsModel
import com.zemingo.drinksmenu.ui.models.IngredientDetailsUiModel
import java.util.function.Function

class IngredientDetailsMapperUi : Function<IngredientDetailsModel, IngredientDetailsUiModel> {

    override fun apply(t: IngredientDetailsModel): IngredientDetailsUiModel {
        return IngredientDetailsUiModel(
            name = t.name,
            description = t.description,
            image = t.image,
            isAlcoholic = t.isAlcoholic,
            alcoholVolume = t.alcoholVolume?.toString()
        )
    }

}