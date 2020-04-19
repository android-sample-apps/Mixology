package com.zemingo.drinksmenu.view_model.mappers

import com.zemingo.drinksmenu.models.DrinkPreviewItemUiModel
import com.zemingo.drinksmenu.models.DrinkPreviewListModel
import com.zemingo.drinksmenu.models.DrinksPreviewListItemUiModel
import java.util.function.Function

class DrinkPreviewMapperUi : Function<DrinkPreviewListModel, DrinksPreviewListItemUiModel> {

    override fun apply(t: DrinkPreviewListModel): DrinksPreviewListItemUiModel {
        return DrinksPreviewListItemUiModel(
            drinks = t.drinks.map {
                DrinkPreviewItemUiModel(
                    id = it.id,
                    name = it.name,
                    thumbnail = it.thumbnail
                )
            }
        )
    }
}