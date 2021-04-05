package com.yanivsos.mixological.v2.drink.mappers

import android.content.Context
import android.text.SpannableString
import com.yanivsos.mixological.R
import com.yanivsos.mixological.database.DrinkModel
import com.yanivsos.mixological.database.DrinkPreviewModel
import com.yanivsos.mixological.extensions.toKey
import com.yanivsos.mixological.network.response.DrinkPreviewResponse
import com.yanivsos.mixological.network.response.DrinkResponse
import com.yanivsos.mixological.ui.models.DrinkErrorUiModel
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.models.DrinkUiModel
import com.yanivsos.mixological.ui.models.IngredientUiModel
import com.yanivsos.mixological.v2.drink.viewModel.DrinkState
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*

val LOCAL_SPANISH = Locale("es", "ES")

fun DrinkPreviewModel.toUiModel(): DrinkPreviewUiModel {
    return DrinkPreviewUiModel(
        id = id,
        name = name,
        thumbnail = thumbnail,
        isFavorite = isFavorite
    )
}

fun List<DrinkPreviewModel>.toUiModel(): List<DrinkPreviewUiModel> {
    return map { it.toUiModel() }
}

fun List<DrinkModel>.toPreviewModel(): List<DrinkPreviewModel> {
    return map { it.toPreviewModel() }
}

fun DrinkState.Error.toUiModel(): DrinkErrorUiModel {
    return when (throwable) {
        is UnknownHostException -> DrinkErrorUiModel(
            drinkId = drinkId,
            title = R.string.error_title,
            description = R.string.error_description_connectivity,
            lottieAnimation = R.raw.no_connection
        )
        is SocketTimeoutException -> DrinkErrorUiModel(
            drinkId = drinkId,
            title = R.string.error_title,
            description = R.string.error_description_timeout,
            lottieAnimation = R.raw.no_connection
        )
        else -> DrinkErrorUiModel(
            drinkId = drinkId,
            title = R.string.error_title,
            description = R.string.error_description_default,
            lottieAnimation = R.raw.something_went_wrong
        )
    }
}

fun DrinkModel.toPreviewModel(): DrinkPreviewModel {
    return DrinkPreviewModel(
        id = id,
        name = name,
        thumbnail = thumbnail,
        isFavorite = isFavorite
    )
}

fun DrinkModel.toUiModel(context: Context): DrinkUiModel {
    val locale = Locale.getDefault()
    return DrinkUiModel(
        id = id,
        name = mapName(locale),
        instructions = mapInstructions(locale),
        ingredients = mapIngredients(),
        category = category,
        alcoholic = alcoholic,
        glass = glass,
        thumbnail = thumbnail,
        video = video,
        shareText = mapShareText(context),
        isFavorite = isFavorite
    )
}

private fun DrinkModel.mapName(locale: Locale): String {
    return nameLocalsMap.fromLocale(locale) ?: name
}

private fun DrinkModel.mapInstructions(locale: Locale): List<SpannableString> {
    val localizesInstruction = instructionsLocalsMap.fromLocale(locale) ?: instructions
    return localizesInstruction
        .trimIndent()
        .split(". ")
        .map { instruction ->
            SpannableString("$instruction.")
        }
}


private fun DrinkModel.mapIngredients(): List<IngredientUiModel> =
    ingredients
        .map {
            IngredientUiModel(
                name = it.key,
                quantity = it.value.parseQuantity()
            )
        }

private fun String.parseQuantity(): String {
    return trim()
        .replace("\n", "")
}

private fun DrinkModel.mapShareText(context: Context): String {
    return StringBuilder().apply {
        append(name)
        append("\n\n")
        ingredients.run {
            if (isNotEmpty()) {
                append(context.getString(R.string.ingredients))
                append("\n")
                forEach { (ingredient, quantity) ->
                    append("■ ")
                    append(ingredient)
                    if (quantity.isNotBlank()) {
                        append(" - ")
                        append(quantity)
                    }
                    append("\n")
                }
                append("\n")
            }
        }
        append(context.getString(R.string.method))
        append(" - \n")
        append(instructions)
    }.toString()
}

private fun Map<String, String?>.fromLocale(locale: Locale): String? {
    return get(locale.toKey())
}

fun DrinkPreviewResponse.toModel(): DrinkPreviewModel {
    return DrinkPreviewModel(
        id = idDrink,
        name = strDrink,
        thumbnail = strDrinkThumb,
        isFavorite = false
    )
}

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
