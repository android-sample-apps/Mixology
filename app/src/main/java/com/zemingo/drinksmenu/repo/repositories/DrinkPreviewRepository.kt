package com.zemingo.drinksmenu.repo.repositories

import com.zemingo.drinksmenu.models.DrinkPreviewModel
import com.zemingo.drinksmenu.models.DrinkPreviewResponse
import com.zemingo.drinksmenu.models.DrinksWrapperResponse
import com.zemingo.drinksmenu.repo.CocktailService
import com.zemingo.drinksmenu.repo.reactive_store.ReactiveStore
import kotlinx.coroutines.flow.Flow
import java.util.function.Function

class DrinkPreviewRepository(
    private val service: CocktailService,
    private val reactiveStore: ReactiveStore<DrinkPreviewModel>,
    private val mapper: Function<DrinksWrapperResponse<DrinkPreviewResponse>, List<DrinkPreviewModel>>
) {

    fun getAll(): Flow<List<DrinkPreviewModel>> {
        return reactiveStore.getAll()
    }

    fun getByCategory(category: String): Flow<List<DrinkPreviewModel>> {
        return reactiveStore.getAll()
    }

    suspend fun fetchByCategory(category: String) {
        val response = service.getByCategory(category.replace(" ", "_"))
        val entities = mapper.apply(response)
        reactiveStore.storeAll(entities)
    }

    suspend fun fetchByCategoryImmediate(category: String): List<DrinkPreviewModel> {
        val response = service.getByCategory(category.replace(" ", "_"))
        return mapper.apply(response)
    }

}