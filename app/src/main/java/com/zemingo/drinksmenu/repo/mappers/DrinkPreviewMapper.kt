package com.zemingo.drinksmenu.repo.mappers

import com.zemingo.drinksmenu.models.*
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