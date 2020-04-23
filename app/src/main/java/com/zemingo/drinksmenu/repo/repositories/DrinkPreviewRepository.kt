package com.zemingo.drinksmenu.repo.repositories

import com.zemingo.drinksmenu.models.*
import com.zemingo.drinksmenu.repo.CocktailService
import com.zemingo.drinksmenu.repo.reactive_store.DrinkPreviewReactiveStore
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

    fun storeAll(drinkPreviews: List<DrinkPreviewModel>) {
        reactiveStore.storeAll(drinkPreviews)
    }

    suspend fun fetchByCategoryImmediate(category: String): List<DrinkPreviewModel> {
        val response = service.getByCategory(category.replace(" ", "_"))
        return mapper.apply(response).apply { storeAll(this) }
    }

}