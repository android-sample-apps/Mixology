package com.zemingo.cocktailmenu.view_model.mappers

import com.zemingo.cocktailmenu.models.DrinkPreviewItemUiModel
import com.zemingo.cocktailmenu.models.DrinkPreviewListModel
import com.zemingo.cocktailmenu.models.DrinksPreviewListItemUiModel
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