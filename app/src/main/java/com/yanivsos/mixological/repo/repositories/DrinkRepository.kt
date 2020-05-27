package com.yanivsos.mixological.repo.repositories

import com.yanivsos.mixological.domain.models.DrinkModel
import com.yanivsos.mixological.repo.DrinkService
import com.yanivsos.mixological.repo.models.DrinkResponse
import com.yanivsos.mixological.repo.models.DrinksWrapperResponse
import com.yanivsos.mixological.repo.reactiveStore.DrinkParams
import com.yanivsos.mixological.repo.reactiveStore.ReactiveStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.function.Function

class DrinkRepository(
    private val service: DrinkService,
    private val drinksReactiveStore: ReactiveStore<String, DrinkModel, DrinkParams>,
    private val mapper: Function<DrinksWrapperResponse<DrinkResponse>, DrinkModel>
) {
    suspend fun fetch(id: String): DrinkModel {
        val response = service.getDrinkById(id)
        return mapper.apply(response)
    }

    fun get(id: String): Flow<DrinkModel?> {
        return drinksReactiveStore
            .getByParam(DrinkParams.ById(id))
            .map { it.firstOrNull() }
    }

    fun store(drinkModel: DrinkModel) {
        drinksReactiveStore.storeAll(listOf(drinkModel))
    }
}