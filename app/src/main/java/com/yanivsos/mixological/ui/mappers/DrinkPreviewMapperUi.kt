package com.yanivsos.mixological.ui.mappers

import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import java.util.function.Function

class DrinkPreviewMapperUi : Function<List<DrinkPreviewModel>, List<DrinkPreviewUiModel>> {

    override fun apply(t: List<DrinkPreviewModel>): List<DrinkPreviewUiModel> {
        return t.map {
            DrinkPreviewUiModel(
                id = it.id,
                name = it.name,
                thumbnail = it.thumbnail,
                isFavorite = it.isFavorite
            )
        }
    }
}