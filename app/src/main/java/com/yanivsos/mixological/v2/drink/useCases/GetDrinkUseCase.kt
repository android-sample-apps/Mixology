package com.yanivsos.mixological.v2.drink.useCases

import com.yanivsos.mixological.database.DrinkModel
import com.yanivsos.mixological.v2.drink.repo.DrinkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetDrinkUseCase(
    private val drinkId: String,
    drinkRepository: DrinkRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    val drink: Flow<DrinkModelResult> =
        drinkRepository
            .getById(drinkId)
            .map { mapToResult(it) }

    private suspend fun mapToResult(drinkModel: DrinkModel?): DrinkModelResult {
        return withContext(defaultDispatcher) {
            drinkModel?.let { DrinkModelResult.Found(it) } ?: DrinkModelResult.NotFount(drinkId)
        }
    }
}

sealed class DrinkModelResult {
    data class Found(val drinkModel: DrinkModel) : DrinkModelResult()
    data class NotFount(val drinkId: String) : DrinkModelResult()
}
