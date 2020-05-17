package com.zemingo.drinksmenu.repo.mappers

import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.repo.models.DrinkResponse
import com.zemingo.drinksmenu.repo.models.DrinksWrapperResponse
import com.zemingo.drinksmenu.repo.models.NullableDrinksWrapperResponse
import java.util.function.Function

class SearchDrinkMapper(
    private val singleDrinkMapper: Function<DrinkResponse, DrinkModel>
) : Function<NullableDrinksWrapperResponse<DrinkResponse>, List<DrinkModel>> {

    override fun apply(t: NullableDrinksWrapperResponse<DrinkResponse>): List<DrinkModel> {
        return t.data?.map {
            singleDrinkMapper.apply(it)
        } ?: emptyList()
    }
}

class SearchDrinkPreviewMapper(
    private val singleDrinkMapper: Function<DrinkResponse, DrinkModel>
) : Function<NullableDrinksWrapperResponse<DrinkResponse>, List<DrinkPreviewModel>> {

    override fun apply(t: NullableDrinksWrapperResponse<DrinkResponse>): List<DrinkPreviewModel> {
        return t.data
            ?.map {
                singleDrinkMapper.apply(it)
            }?.map { DrinkPreviewModel(it) } ?: emptyList()
    }
}

class DrinkMapper(
    private val singleDrinkMapper: Function<DrinkResponse, DrinkModel>
) : Function<DrinksWrapperResponse<DrinkResponse>, DrinkModel> {

    override fun apply(t: DrinksWrapperResponse<DrinkResponse>): DrinkModel {
        return singleDrinkMapper.apply(
            t.data.first()
        )
    }
}

class SingleDrinkMapper : Function<DrinkResponse, DrinkModel> {
    override fun apply(t: DrinkResponse): DrinkModel {
        return DrinkModel(
            id = t.idDrink,
            name = t.strDrink,
            ingredients = ingredientMap(t),
            instructions = t.strInstructions,
            thumbnail = t.strDrinkThumb,
            alcoholic = t.strAlcoholic,
            category = t.strCategory,
            glass = t.strGlass,
            video = t.strVideo,
            isFavorite = false
        )
    }

    private fun ingredientMap(drinkResponse: DrinkResponse): Map<String, String?> {
        return mutableMapOf<String, String?>()
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

    private fun MutableMap<String, String?>.putIfValid(ingredient: String?, measurement: String?) {
        if (ingredient != null) {
            put(ingredient, measurement)
        }
    }
}