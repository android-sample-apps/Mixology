package com.zemingo.drinksmenu.repo.mappers

import com.zemingo.drinksmenu.models.*
import java.util.function.Function

class DrinkMapper(
    private val ingredientsMapper: Function<FullDrinkResponse, IngredientListModel>
) : Function<FullDrinkResponse, DrinkModel> {

    override fun apply(t: FullDrinkResponse): DrinkModel {
        return DrinkModel(
            id = t.idDrink,
            name = t.strDrink,
            instructions = InstructionModel(t.strInstructions),
            ingredients = ingredientsMapper.apply(t),
            thumbnail = ImageModel(t.strDrinkThumb),
            dateModified = DateModel(t.dateModified)
        )
    }
}