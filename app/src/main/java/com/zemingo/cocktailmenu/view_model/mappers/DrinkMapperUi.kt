package com.zemingo.cocktailmenu.view_model.mappers

import com.zemingo.cocktailmenu.R
import com.zemingo.cocktailmenu.models.DrinkItemUiModel
import com.zemingo.cocktailmenu.models.DrinkModel
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