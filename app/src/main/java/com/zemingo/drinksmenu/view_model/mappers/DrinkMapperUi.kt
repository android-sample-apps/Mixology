package com.zemingo.drinksmenu.view_model.mappers

import com.zemingo.drinksmenu.R
import com.zemingo.drinksmenu.models.DrinkItemUiModel
import com.zemingo.drinksmenu.models.DrinkModel
import java.util.function.Function

class DrinkMapperUi : Function<DrinkModel, DrinkItemUiModel> {

    override fun apply(t: DrinkModel): DrinkItemUiModel {
        return DrinkItemUiModel(
            name = t.name,
            ingredients = parseIngredients(t),
            glassIcon = R.drawable.ic_glass_beer,
            thumbnail = t.thumbnail
        )
    }

    private fun parseIngredients(t: DrinkModel) =
        t.ingredients
            .ingredients
            .joinToString(
                separator = ", "
            ) { it.ingredient }
}