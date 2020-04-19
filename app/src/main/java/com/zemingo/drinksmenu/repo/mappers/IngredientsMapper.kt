package com.zemingo.drinksmenu.repo.mappers

import com.zemingo.drinksmenu.models.FullDrinkResponse
import com.zemingo.drinksmenu.models.IngredientListModel
import com.zemingo.drinksmenu.models.IngredientModel
import java.util.function.Function

class IngredientsMapper : Function<FullDrinkResponse, IngredientListModel> {

    override fun apply(t: FullDrinkResponse): IngredientListModel {
        return IngredientListModel(
            ingredients = generateIngredientsList(t)
        )
    }

    private fun generateIngredientsList(t: FullDrinkResponse): List<IngredientModel> {
        return mutableListOf<IngredientModel?>()
            .apply {
                add(ingredientModel(t.strIngredient1, t.strMeasure1))
                add(ingredientModel(t.strIngredient2, t.strMeasure2))
                add(ingredientModel(t.strIngredient3, t.strMeasure3))
                add(ingredientModel(t.strIngredient4, t.strMeasure4))
                add(ingredientModel(t.strIngredient5, t.strMeasure5))
                add(ingredientModel(t.strIngredient6, t.strMeasure6))
                add(ingredientModel(t.strIngredient7, t.strMeasure7))
                add(ingredientModel(t.strIngredient8, t.strMeasure8))
                add(ingredientModel(t.strIngredient9, t.strMeasure9))
                add(ingredientModel(t.strIngredient10, t.strMeasure10))
                add(ingredientModel(t.strIngredient11, t.strMeasure11))
                add(ingredientModel(t.strIngredient12, t.strMeasure12))
                add(ingredientModel(t.strIngredient13, t.strMeasure13))
                add(ingredientModel(t.strIngredient14, t.strMeasure14))
                add(ingredientModel(t.strIngredient15, t.strMeasure15))
            }.filterNotNull()
    }

    private fun ingredientModel(strIngredient: String?, strMeasure: String?): IngredientModel? {
        if (strIngredient == null || strMeasure == null) {
            return null
        }
        return IngredientModel(
            ingredient = strIngredient,
            measurement = strMeasure
        )
    }
}