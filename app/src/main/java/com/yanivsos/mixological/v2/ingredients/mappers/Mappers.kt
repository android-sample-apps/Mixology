package com.yanivsos.mixological.v2.ingredients.mappers

import com.yanivsos.mixological.database.IngredientDetailsModel
import com.yanivsos.mixological.network.response.IngredientDetailsResponse
import com.yanivsos.mixological.network.response.IngredientsWrapperResponse
import com.yanivsos.mixological.ui.models.IngredientDetailsUiModel
import java.net.URLEncoder
import java.util.*

fun IngredientsWrapperResponse<IngredientDetailsResponse>.toModel(): IngredientDetailsModel {
    return data.first().let {
        IngredientDetailsModel(
            id = it.id,
            name = it.name,
            description = it.description,
            image = encodeImageUrl(it.name),
            alcoholVolume = it.alcoholVolume?.toIntOrNull(),
            isAlcoholic = it.isAlcoholic?.lowercase(Locale.ROOT)?.equals("yes") == true
        )
    }
}

private fun encodeImageUrl(ingredientName: String): String {
    val imageName = URLEncoder.encode(ingredientName, "utf-8")
        .replace("+", "%20")
    return "https://www.thecocktaildb.com/images/ingredients/$imageName.png"
}


fun IngredientDetailsModel.toUiModel(): IngredientDetailsUiModel {
    return IngredientDetailsUiModel(
        name = name,
        description = description,
        image = image,
        isAlcoholic = isAlcoholic,
        alcoholVolume = alcoholVolume?.toString()
    )
}
