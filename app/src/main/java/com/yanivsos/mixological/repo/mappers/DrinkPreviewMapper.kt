package com.yanivsos.mixological.repo.mappers

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.repo.models.DrinkPreviewResponse
import com.yanivsos.mixological.repo.models.DrinksWrapperResponse
import java.util.function.Function

class DrinkPreviewMapper :
    Function<DrinksWrapperResponse<DrinkPreviewResponse>, List<DrinkPreviewModel>> {

    override fun apply(t: DrinksWrapperResponse<DrinkPreviewResponse>): List<DrinkPreviewModel> {
        return t.data.map {
            DrinkPreviewModel(
                id = it.idDrink,
                name = it.strDrink,
                thumbnail = it.strDrinkThumb,
                isFavorite = false
            )
        }
    }
}