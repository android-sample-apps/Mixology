package com.zemingo.drinksmenu.repo.repositories

import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.repo.models.DrinkPreviewResponse
import com.zemingo.drinksmenu.repo.models.DrinksWrapperResponse
import com.zemingo.drinksmenu.repo.CocktailService
import com.zemingo.drinksmenu.repo.reactive_store.DrinkPreviewParam
import com.zemingo.drinksmenu.repo.reactive_store.ReactiveStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.function.Function

class DrinkPreviewRepository(
    private val service: CocktailService,
    private val reactiveStore: ReactiveStore<String, DrinkPreviewModel, DrinkPreviewParam>,
    private val mapper: Function<DrinksWrapperResponse<DrinkPreviewResponse>, List<DrinkPreviewModel>>
) {

    fun getAll(): Flow<List<DrinkPreviewModel>> {
        return reactiveStore.getAll()
    }

    fun storeAll(drinkPreviews: List<DrinkPreviewModel>) {
        reactiveStore.storeAll(drinkPreviews)
    }

    fun mostPopular(): Flow<List<DrinkPreviewModel>> {
        //todo - replace with real API
        return reactiveStore.getAll().map { it.take(5) }
    }

    fun latestArrivals(): Flow<List<DrinkPreviewModel>> {
        //todo - replace with real API
        return reactiveStore.getAll().map { it.takeLast(5) }
    }

    suspend fun fetchByCategoryImmediate(category: String): List<DrinkPreviewModel> {
        val response = service.getByCategory(category.replace(" ", "_"))
        return mapper.apply(response).apply { storeAll(this) }
    }

}