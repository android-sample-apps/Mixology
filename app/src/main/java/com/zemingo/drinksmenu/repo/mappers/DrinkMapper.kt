package com.zemingo.drinksmenu.repo.mappers

import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.repo.models.DrinkResponse
import com.zemingo.drinksmenu.repo.models.DrinksWrapperResponse
import java.util.function.Function

class DrinkMapper : Function<DrinksWrapperResponse<DrinkResponse>, DrinkModel> {
    override fun apply(t: DrinksWrapperResponse<DrinkResponse>): DrinkModel {
        with(t.data.first()) {
            return DrinkModel(
                id = idDrink,
                name = strDrink,
                ingredients = ingredientMap(this),
                instructions = strInstructions,
                thumbnail = strDrinkThumb
            )
        }
    }

    private fun ingredientMap(drinkResponse: DrinkResponse): Map<String, String> {
        return mutableMapOf<String, String>()
            .apply {
                putIfValid(drinkResponse.strIngredient1, drinkResponse.strMeasure1)
                putIfValid(drinkResponse.strIngredient2, drinkResponse.strMeasure2)
                putIfValid(drinkResponse.strIngredient3, drinkResponse.strMeasure3)
                putIfValid(drinkResponse.strIngredient4, drinkResponse.strMeasure4)
                putIfValid(drinkResponse.strIngredient5, drinkResponse.strMeasure5)
                putIfValid(drinkResponse.strIngredient6, drinkResponse.strMeasure6)
                putIfValid(drinkResponse.strIngredient7, drinkResponse.strMeasure7)
                putIfValid(drinkResponse.strIngredient8, drinkResponse.strMeasure8)
                putIfValid(drinkResponse.strIngredient9, drinkResponse.strMeasure9)
                putIfValid(drinkResponse.strIngredient10, drinkResponse.strMeasure10)
                putIfValid(drinkResponse.strIngredient11, drinkResponse.strMeasure11)
                putIfValid(drinkResponse.strIngredient12, drinkResponse.strMeasure12)
                putIfValid(drinkResponse.strIngredient13, drinkResponse.strMeasure13)
                putIfValid(drinkResponse.strIngredient14, drinkResponse.strMeasure14)
                putIfValid(drinkResponse.strIngredient15, drinkResponse.strMeasure15)
            }
    }

    private fun MutableMap<String, String>.putIfValid(ingredient: String?, measurement: String?) {
        if (ingredient != null && measurement != null) {
            put(ingredient, measurement)
        }
    }
}