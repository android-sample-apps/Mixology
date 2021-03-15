package com.yanivsos.mixological.v2.drink.mappers

import com.yanivsos.mixological.domain.models.DrinkModel
import com.yanivsos.mixological.repo.models.DrinkResponse
import com.yanivsos.mixological.repo.models.DrinksWrapperResponse

fun Collection<DrinkResponse>.toModel(): List<DrinkModel> {
    return this.map { it.toModel() }
}

fun DrinksWrapperResponse<DrinkResponse>.toModel(): List<DrinkModel> {
    return this.data.toModel()
}

fun DrinksWrapperResponse<DrinkResponse>.toFirstOrNullModel(): DrinkModel? {
    return this.data.firstOrNull()?.toModel()
}
