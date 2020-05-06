package com.zemingo.drinksmenu.ui.mappers

import android.text.SpannableString
import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.ui.models.DrinkUiModel
import com.zemingo.drinksmenu.ui.models.IngredientUiModel
import java.util.function.Function

class DrinkMapperUi : Function<DrinkModel, DrinkUiModel> {

    override fun apply(t: DrinkModel): DrinkUiModel {
        return DrinkUiModel(
            id = t.id,
            name = t.name,
            instructions = mapInstructions(t),
            ingredients = mapIngredients(t),
            category = t.category,
            alcoholic = t.alcoholic,
            glass = t.glass,
            thumbnail = t.thumbnail,
            video = t.video
        )
    }

    private fun mapInstructions(t: DrinkModel): List<SpannableString> {
        return t.instructions
            .trimIndent()
            .split(". ")
            .map { instruction ->
                SpannableString("$instruction.").apply {
//                    if (instruction.firstOrNull()?.isLetter() == true) {
//                        setSpan(RelativeSizeSpan(1.5f), 0, 1, 0)
//                    }
                }
            }
    }

    private fun mapIngredients(t: DrinkModel): List<IngredientUiModel> =
        t.ingredients
            .map {
                IngredientUiModel(
                    name = it.key,
                    quantity = it.value
                )
            }
}