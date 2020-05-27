package com.yanivsos.mixological.ui.mappers

import com.yanivsos.mixological.domain.models.IngredientDetailsModel
import com.yanivsos.mixological.ui.models.IngredientDetailsUiModel
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