package com.zemingo.drinksmenu.repo.mappers

import com.zemingo.drinksmenu.domain.models.IngredientDetailsModel
import com.zemingo.drinksmenu.repo.models.IngredientDetailsResponse
import com.zemingo.drinksmenu.repo.models.IngredientsWrapperResponse
import java.net.URLEncoder
import java.util.*
import java.util.function.Function

class IngredientDetailsMapper :
    Function<IngredientsWrapperResponse<IngredientDetailsResponse>, List<IngredientDetailsModel>> {

    override fun apply(t: IngredientsWrapperResponse<IngredientDetailsResponse>): List<IngredientDetailsModel> {
        return t.data.map {
            IngredientDetailsModel(
                id = it.id,
                name = it.name,
                description = it.description,
                image = encodeImageUrl(it.name),
                alcoholVolume = it.alcoholVolume?.toIntOrNull(),
                isAlcoholic = it.isAlcoholic?.toLowerCase(Locale.ROOT)?.equals("yes") == true
            )
        }
    }

    private fun encodeImageUrl(ingredientName: String): String {
        val imageName = URLEncoder.encode(ingredientName, "utf-8")
            .replace("+", "%20")
        return "https://www.thecocktaildb.com/images/ingredients/$imageName.png"
    }
}