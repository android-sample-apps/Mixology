package com.yanivsos.mixological.repo.repositories

import com.yanivsos.mixological.repo.models.DrinksWrapperResponse
import com.yanivsos.mixological.domain.models.IngredientModel
import com.yanivsos.mixological.repo.models.IngredientResponse
import com.yanivsos.mixological.repo.DrinkService
import com.yanivsos.mixological.repo.reactiveStore.ReactiveStore
import kotlinx.coroutines.flow.Flow
import java.util.function.Function

class IngredientRepository(
    private val service: DrinkService,
    private val reactiveStore: ReactiveStore<String, IngredientModel, Void>,
    private val mapper: Function<DrinksWrapperResponse<IngredientResponse>, List<IngredientModel>>
) {

    fun getAll(): Flow<List<IngredientModel>> {
        return reactiveStore.getAll()
    }

    suspend fun fetchIngredients() {
        val response = service.getIngredientList()
        val model = mapper.apply(response)
        reactiveStore.storeAll(model)
    }
}