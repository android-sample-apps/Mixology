package com.zemingo.drinksmenu.view_model.mappers

import com.zemingo.drinksmenu.models.DrinkPreviewUiModel
import com.zemingo.drinksmenu.models.DrinkPreviewModel
import java.util.function.Function

class DrinkPreviewMapperUi : Function<List<DrinkPreviewModel>, List<DrinkPreviewUiModel>> {

    override fun apply(t: List<DrinkPreviewModel>): List<DrinkPreviewUiModel> {
        return t.map {
            DrinkPreviewUiModel(
                id = it.id,
                name = it.name,
                thumbnail = it.thumbnail
            )
        }
    }
}