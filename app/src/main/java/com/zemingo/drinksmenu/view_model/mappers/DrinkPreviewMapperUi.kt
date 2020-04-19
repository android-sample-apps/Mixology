package com.zemingo.drinksmenu.view_model.mappers

import com.zemingo.drinksmenu.models.DrinkPreviewUiModel
import com.zemingo.drinksmenu.models.DrinkPreviewListModel
import com.zemingo.drinksmenu.models.DrinksPreviewListItemUiModel
import java.util.function.Function

class DrinkPreviewMapperUi : Function<DrinkPreviewListModel, DrinksPreviewListItemUiModel> {

    override fun apply(t: DrinkPreviewListModel): DrinksPreviewListItemUiModel {
        return DrinksPreviewListItemUiModel(
            drinks = t.drinks.map {
                DrinkPreviewUiModel(
                    id = it.id,
                    name = it.name,
                    thumbnail = it.thumbnail
                )
            }
        )
    }
}