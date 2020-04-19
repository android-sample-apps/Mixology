package com.zemingo.drinksmenu.repo.mappers

import com.zemingo.drinksmenu.models.*
import java.util.function.Function

class DrinkPreviewMapper : Function<DrinksWrapperResponse<DrinkResponse>, DrinkPreviewListModel> {

    override fun apply(t: DrinksWrapperResponse<DrinkResponse>): DrinkPreviewListModel {
        return DrinkPreviewListModel(
            drinks = t.data.map {
                DrinkPreviewModel(
                    id = it.idDrink,
                    name = it.strDrink,
                    thumbnail = it.strDrinkThumb
                )
            }
        )
    }
}