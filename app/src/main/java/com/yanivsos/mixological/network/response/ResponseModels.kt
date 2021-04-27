package com.yanivsos.mixological.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
    @SerialName("strCategory") val category: String
)

@Serializable
data class IngredientResponse(
    @SerialName("strIngredient1") val name: String
)

@Serializable
data class IngredientDetailsResponse(
    @SerialName("idIngredient") val id: String,
    @SerialName("strIngredient") val name: String,
    @SerialName("strDescription") val description: String?,
    @SerialName("strAlcohol") val isAlcoholic: String?,
    @SerialName("strABV") val alcoholVolume: String?
)

@Serializable
data class AlcoholicFilterResponse(
    @SerialName("strAlcoholic") val strAlcoholic: String?
)

@Serializable
data class GlassResponse(
    @SerialName("strGlass") val strGlass: String?
)

@Serializable
data class DrinkPreviewResponse(
    @SerialName("idDrink") val idDrink: String,
    @SerialName("strDrink") val strDrink: String,
    @SerialName("strDrinkThumb") val strDrinkThumb: String
)

@Serializable
data class DrinkResponse(
    @SerialName("idDrink") val idDrink: String,
    @SerialName("strDrink") val strDrink: String,
    @SerialName("strDrinkES") val strDrinkES: String? = null,
    @SerialName("strDrinkDE") val strDrinkDE: String? = null,
    @SerialName("strDrinkFR") val strDrinkFR: String? = null,
//    @SerialName("strDrinkZH-HANS") val strDrinkZH_HANS: String?,
//    @SerialName("strDrinkZH-HANT") val strDrinkZH_HANT: String?,
    @SerialName("strGlass") val strGlass: String,
    @SerialName("strAlcoholic") val strAlcoholic: String?,
    @SerialName("strVideo") val strVideo: String?,
    @SerialName("strInstructions") val strInstructions: String,
    @SerialName("strInstructionsES") val strInstructionsES: String?,
    @SerialName("strInstructionsDE") val strInstructionsDE: String?,
    @SerialName("strInstructionsFR") val strInstructionsFR: String?,
//    @SerialName("strInstructionsZH-HANS") val strInstructionsZH_HANS: String,
//    @SerialName("strInstructionsZH-HANT") val strInstructionsZH_HANT: String,
    @SerialName("strCategory") val strCategory: String,
    @SerialName("strDrinkThumb") val strDrinkThumb: String,
    @SerialName("strIngredient1") val strIngredient1: String?,
    @SerialName("strIngredient2") val strIngredient2: String?,
    @SerialName("strIngredient3") val strIngredient3: String?,
    @SerialName("strIngredient4") val strIngredient4: String?,
    @SerialName("strIngredient5") val strIngredient5: String?,
    @SerialName("strIngredient6") val strIngredient6: String?,
    @SerialName("strIngredient7") val strIngredient7: String?,
    @SerialName("strIngredient8") val strIngredient8: String?,
    @SerialName("strIngredient9") val strIngredient9: String?,
    @SerialName("strIngredient10") val strIngredient10: String?,
    @SerialName("strIngredient11") val strIngredient11: String?,
    @SerialName("strIngredient12") val strIngredient12: String?,
    @SerialName("strIngredient13") val strIngredient13: String?,
    @SerialName("strIngredient14") val strIngredient14: String?,
    @SerialName("strIngredient15") val strIngredient15: String?,
    @SerialName("strMeasure1") val strMeasure1: String?,
    @SerialName("strMeasure2") val strMeasure2: String?,
    @SerialName("strMeasure3") val strMeasure3: String?,
    @SerialName("strMeasure4") val strMeasure4: String?,
    @SerialName("strMeasure5") val strMeasure5: String?,
    @SerialName("strMeasure6") val strMeasure6: String?,
    @SerialName("strMeasure7") val strMeasure7: String?,
    @SerialName("strMeasure8") val strMeasure8: String?,
    @SerialName("strMeasure9") val strMeasure9: String?,
    @SerialName("strMeasure10") val strMeasure10: String?,
    @SerialName("strMeasure11") val strMeasure11: String?,
    @SerialName("strMeasure12") val strMeasure12: String?,
    @SerialName("strMeasure13") val strMeasure13: String?,
    @SerialName("strMeasure14") val strMeasure14: String?,
    @SerialName("strMeasure15") val strMeasure15: String?,
    @SerialName("dateModified") val dateModified: String? = null
)

@Serializable
data class DrinksWrapperResponse<T>(
    @SerialName(SERIALIZED_NAME_DRINKS) val data: List<T>
)

@Serializable(with = FilteredDrinkPreviewsSerializer::class)
sealed class FilterDrinkPreviewResponse {

    @Serializable
    data class Results(
        @SerialName(SERIALIZED_NAME_DRINKS)
        val drinks: List<DrinkPreviewResponse>
    ) : FilterDrinkPreviewResponse()

    @Serializable
    data class NoResults(
        @SerialName(SERIALIZED_NAME_DRINKS)
        val message: String
    ) : FilterDrinkPreviewResponse()
}


@Serializable
data class NullableDrinksWrapperResponse<T>(
    @SerialName(SERIALIZED_NAME_DRINKS) val data: List<T>?
)

@Serializable
data class IngredientsWrapperResponse<T>(
    @SerialName("ingredients") val data: List<T>
)

const val SERIALIZED_NAME_DRINKS = "drinks"
