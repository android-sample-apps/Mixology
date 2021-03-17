package com.yanivsos.mixological.v2.drink.useCases

import com.yanivsos.mixological.domain.models.DrinkModel
import com.yanivsos.mixological.v2.drink.repo.DrinkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetDrinkUseCase(
    private val drinkId: String,
    drinkRepository: DrinkRepository
) {
    val drink: Flow<DrinkModelResult> =
        drinkRepository
            .getById(drinkId)
            .map { mapToResult(it) }

    private fun mapToResult(drinkModel: DrinkModel?): DrinkModelResult {
        return drinkModel?.let { DrinkModelResult.Found(it) } ?: DrinkModelResult.NotFount(drinkId)
    }
}

sealed class DrinkModelResult {
    data class Found(val drinkModel: DrinkModel) : DrinkModelResult()
    data class NotFount(val drinkId: String) : DrinkModelResult()
}
