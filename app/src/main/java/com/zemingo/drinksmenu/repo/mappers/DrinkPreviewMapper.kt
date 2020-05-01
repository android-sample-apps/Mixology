package com.zemingo.drinksmenu.repo.mappers

import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.repo.models.DrinkPreviewResponse
import com.zemingo.drinksmenu.repo.models.DrinksWrapperResponse
import java.util.function.Function

class DrinkPreviewMapper :
    Function<DrinksWrapperResponse<DrinkPreviewResponse>, List<DrinkPreviewModel>> {

    override fun apply(t: DrinksWrapperResponse<DrinkPreviewResponse>): List<DrinkPreviewModel> {
        return t.data.map {
            DrinkPreviewModel(
                id = it.idDrink,
                name = it.strDrink,
                thumbnail = it.strDrinkThumb
            )
        }
    }
}