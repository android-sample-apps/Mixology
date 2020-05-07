package com.zemingo.drinksmenu.ui.mappers

import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import com.zemingo.drinksmenu.ui.models.DrinkUiModel
import java.util.function.Function

class DrinkToPreviewUiMapper : Function<List<DrinkUiModel>, List<DrinkPreviewUiModel>> {
    override fun apply(t: List<DrinkUiModel>): List<DrinkPreviewUiModel> {
        return t.map {
            DrinkPreviewUiModel(
                id = it.id,
                name = it.name,
                thumbnail = it.thumbnail
            )
        }
    }
}