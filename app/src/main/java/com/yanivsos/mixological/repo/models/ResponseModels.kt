package com.yanivsos.mixological.repo.models

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("strCategory") val category: String
)

data class IngredientResponse(
    @SerializedName("strIngredient1") val name: String
)

data class IngredientDetailsResponse(
    @SerializedName("idIngredient") val id: String,
    @SerializedName("strIngredient") val name: String,
    @SerializedName("strDescription") val description: String,
    @SerializedName("strAlcohol") val isAlcoholic: String?,
    @SerializedName("strABV") val alcoholVolume: String?
)

data class AlcoholicFilterResponse(
    @SerializedName("strAlcoholic") val strAlcoholic: String?
)

data class GlassResponse(
    @SerializedName("strGlass") val strGlass: String?
)

data class DrinkPreviewResponse(
    @SerializedName("idDrink") val idDrink: String,
    @SerializedName("strDrink") val strDrink: String,
    @SerializedName("strDrinkThumb") val strDrinkThumb: String
)

data class DrinkResponse(
    @SerializedName("idDrink") val idDrink: String,
    @SerializedName("strDrink") val strDrink: String,
    @SerializedName("strDrinkES") val strDrinkES: String?,
    @SerializedName("strDrinkDE") val strDrinkDE: String?,
    @SerializedName("strDrinkFR") val strDrinkFR: String?,
    @SerializedName("strDrinkZH-HANS") val strDrinkZH_HANS: String?,
    @SerializedName("strDrinkZH-HANT") val strDrinkZH_HANT: String?,
    @SerializedName("strGlass") val strGlass: String,
    @SerializedName("strAlcoholic") val strAlcoholic: String?,
    @SerializedName("strVideo") val strVideo: String?,
    @SerializedName("strInstructions") val strInstructions: String,
    @SerializedName("strInstructionsES") val strInstructionsES: String,
    @SerializedName("strInstructionsDE") val strInstructionsDE: String,
    @SerializedName("strInstructionsFR") val strInstructionsFR: String,
    @SerializedName("strInstructionsZH-HANS") val strInstructionsZH_HANS: String,
    @SerializedName("strInstructionsZH-HANT") val strInstructionsZH_HANT: String,
    @SerializedName("strCategory") val strCategory: String,
    @SerializedName("strDrinkThumb") val strDrinkThumb: String,
    @SerializedName("strIngredient1") val strIngredient1: String?,
    @SerializedName("strIngredient2") val strIngredient2: String?,
    @SerializedName("strIngredient3") val strIngredient3: String?,
    @SerializedName("strIngredient4") val strIngredient4: String?,
    @SerializedName("strIngredient5") val strIngredient5: String?,
    @SerializedName("strIngredient6") val strIngredient6: String?,
    @SerializedName("strIngredient7") val strIngredient7: String?,
    @SerializedName("strIngredient8") val strIngredient8: String?,
    @SerializedName("strIngredient9") val strIngredient9: String?,
    @SerializedName("strIngredient10") val strIngredient10: String?,
    @SerializedName("strIngredient11") val strIngredient11: String?,
    @SerializedName("strIngredient12") val strIngredient12: String?,
    @SerializedName("strIngredient13") val strIngredient13: String?,
    @SerializedName("strIngredient14") val strIngredient14: String?,
    @SerializedName("strIngredient15") val strIngredient15: String?,
    @SerializedName("strMeasure1") val strMeasure1: String?,
    @SerializedName("strMeasure2") val strMeasure2: String?,
    @SerializedName("strMeasure3") val strMeasure3: String?,
    @SerializedName("strMeasure4") val strMeasure4: String?,
    @SerializedName("strMeasure5") val strMeasure5: String?,
    @SerializedName("strMeasure6") val strMeasure6: String?,
    @SerializedName("strMeasure7") val strMeasure7: String?,
    @SerializedName("strMeasure8") val strMeasure8: String?,
    @SerializedName("strMeasure9") val strMeasure9: String?,
    @SerializedName("strMeasure10") val strMeasure10: String?,
    @SerializedName("strMeasure11") val strMeasure11: String?,
    @SerializedName("strMeasure12") val strMeasure12: String?,
    @SerializedName("strMeasure13") val strMeasure13: String?,
    @SerializedName("strMeasure14") val strMeasure14: String?,
    @SerializedName("strMeasure15") val strMeasure15: String?,
    @SerializedName("dateModified") val dateModified: String
)

data class DrinksWrapperResponse<T>(
    @SerializedName("drinks") val data: List<T>
)

data class NullableDrinksWrapperResponse<T>(
    @SerializedName("drinks") val data: List<T>?
)

data class IngredientsWrapperResponse<T>(
    @SerializedName("ingredients") val data: List<T>
)