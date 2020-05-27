package com.yanivsos.mixological.ui.mappers

import com.yanivsos.mixological.domain.models.IngredientModel
import com.yanivsos.mixological.ui.models.IngredientFilterUiModel
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