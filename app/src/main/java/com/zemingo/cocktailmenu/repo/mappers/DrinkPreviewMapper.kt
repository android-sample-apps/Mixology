package com.zemingo.cocktailmenu.repo.mappers

import com.zemingo.cocktailmenu.models.DrinkPreviewListModel
import com.zemingo.cocktailmenu.models.DrinkPreviewModel
import com.zemingo.cocktailmenu.models.DrinksListResponse
import com.zemingo.cocktailmenu.models.ImageModel
import java.util.function.Function

class DrinkPreviewMapper : Function<DrinksListResponse, DrinkPreviewListModel> {

    override fun apply(t: DrinksListResponse): DrinkPreviewListModel {
        return DrinkPreviewListModel(
            drinks = t.drinks.map {
                DrinkPreviewModel(
                    id = it.idDrink,
                    name = it.strDrink,
                    thumbnail = ImageModel(it.strDrinkThumb)
                )
            }
        )
    }

}