package com.zemingo.drinksmenu.repo.repositories

import com.zemingo.drinksmenu.repo.models.DrinksWrapperResponse
import com.zemingo.drinksmenu.domain.models.IngredientModel
import com.zemingo.drinksmenu.repo.models.IngredientResponse
import com.zemingo.drinksmenu.repo.DrinkService
import com.zemingo.drinksmenu.repo.reactiveStore.ReactiveStore
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