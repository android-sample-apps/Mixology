package com.yanivsos.mixological.v2.drink.mappers

import com.yanivsos.mixological.domain.models.DrinkModel
import com.yanivsos.mixological.extensions.toKey
import com.yanivsos.mixological.repo.mappers.LOCAL_SPANISH
import com.yanivsos.mixological.repo.models.DrinkResponse
import java.util.*

fun DrinkResponse.toModel(): DrinkModel {
    return DrinkModel(
        id = idDrink,
        name = strDrink,
        ingredients = ingredientMap(),
        instructions = strInstructions,
        thumbnail = strDrinkThumb,
        alcoholic = strAlcoholic,
        category = strCategory,
        glass = strGlass,
        video = strVideo,
        nameLocalsMap = nameLocalsMap(),
        instructionsLocalsMap = instructionsLocalMap()
    )
}

private fun DrinkResponse.ingredientMap(): Map<String, String> {
    return mutableMapOf<String, String>()
        .apply {
            putIfValid(strIngredient1, strMeasure1)
            putIfValid(strIngredient2, strMeasure2)
            putIfValid(strIngredient3, strMeasure3)
            putIfValid(strIngredient4, strMeasure4)
            putIfValid(strIngredient5, strMeasure5)
            putIfValid(strIngredient6, strMeasure6)
            putIfValid(strIngredient7, strMeasure7)
            putIfValid(strIngredient8, strMeasure8)
            putIfValid(strIngredient9, strMeasure9)
            putIfValid(strIngredient10, strMeasure10)
            putIfValid(strIngredient11, strMeasure11)
            putIfValid(strIngredient12, strMeasure12)
            putIfValid(strIngredient13, strMeasure13)
            putIfValid(strIngredient14, strMeasure14)
            putIfValid(strIngredient15, strMeasure15)
        }
}

private fun MutableMap<String, String>.putIfValid(ingredient: String?, measurement: String?) {
    if (ingredient?.isNotBlank() == true) {
        put(ingredient, measurement ?: "")
    }
}

private fun DrinkResponse.instructionsLocalMap(): Map<String, String?> {
    return mutableMapOf<String, String?>().apply {
        put(Locale.GERMAN.toKey(), strInstructionsDE)
        put(Locale.FRENCH.toKey(), strInstructionsFR)
        put(LOCAL_SPANISH.toKey(), strInstructionsES)
    }
}

private fun DrinkResponse.nameLocalsMap(): Map<String, String?> {
    return mutableMapOf<String, String?>().apply {
        put(Locale.GERMAN.toKey(), strDrinkDE)
        put(Locale.FRENCH.toKey(), strDrinkFR)
        put(LOCAL_SPANISH.toKey(), strDrinkES)
    }
}
