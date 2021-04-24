package com.yanivsos.mixological.v2.drink.mappers

import com.yanivsos.mixological.database.DrinkModel
import com.yanivsos.mixological.database.DrinkPreviewModel
import com.yanivsos.mixological.network.response.*

fun DrinksWrapperResponse<DrinkResponse>.toFirstOrNullModel(): DrinkModel? {
    return this.data.firstOrNull()?.toModel()
}

fun DrinksWrapperResponse<DrinkPreviewResponse>.toModel(): List<DrinkPreviewModel> {
    return this.data.map { it.toModel() }
}

fun FilterDrinkPreviewResponse.toModel(): List<DrinkPreviewModel> {
    return when (this) {
        is FilterDrinkPreviewResponse.NoResults -> emptyList()
        is FilterDrinkPreviewResponse.Results -> this.drinks.map { it.toModel() }
    }
}

fun NullableDrinksWrapperResponse<DrinkResponse>.toModel(): List<DrinkModel> {
    return this.data?.map { it.toModel() } ?: emptyList()
}
