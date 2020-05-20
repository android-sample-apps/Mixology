package com.zemingo.drinksmenu.repo.repositories

import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.repo.DrinkService
import com.zemingo.drinksmenu.repo.models.DrinkResponse
import com.zemingo.drinksmenu.repo.models.DrinksWrapperResponse
import com.zemingo.drinksmenu.repo.reactiveStore.DrinkParams
import com.zemingo.drinksmenu.repo.reactiveStore.ReactiveStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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