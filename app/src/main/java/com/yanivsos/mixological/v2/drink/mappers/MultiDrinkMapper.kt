package com.yanivsos.mixological.v2.drink.mappers

import com.yanivsos.mixological.database.DrinkModel
import com.yanivsos.mixological.database.DrinkPreviewModel
import com.yanivsos.mixological.network.response.DrinkPreviewResponse
import com.yanivsos.mixological.network.response.DrinkResponse
import com.yanivsos.mixological.network.response.DrinksWrapperResponse
import com.yanivsos.mixological.network.response.NullableDrinksWrapperResponse

fun DrinksWrapperResponse<DrinkResponse>.toFirstOrNullModel(): DrinkModel? {
    return this.data.firstOrNull()?.toModel()
}

fun DrinksWrapperResponse<DrinkPreviewResponse>.toModel(): List<DrinkPreviewModel> {
    return this.data.map { it.toModel() }
}

fun NullableDrinksWrapperResponse<DrinkResponse>.toModel(): List<DrinkModel> {
    return this.data?.map { it.toModel() } ?: emptyList()
}
