package com.zemingo.drinksmenu.repo.repositories

import com.zemingo.drinksmenu.models.CategoryModel
import com.zemingo.drinksmenu.models.CategoryResponse
import com.zemingo.drinksmenu.models.DrinksWrapperResponse
import com.zemingo.drinksmenu.repo.CocktailService
import com.zemingo.drinksmenu.repo.reactive_store.ReactiveStore
import kotlinx.coroutines.flow.Flow
import java.util.function.Function

class CategoryRepository(
    private val service: CocktailService,
    private val reactiveStore: ReactiveStore<CategoryModel>,
    private val mapper: Function<DrinksWrapperResponse<CategoryResponse>, List<CategoryModel>>
) {

    fun get(): Flow<List<CategoryModel>> {
        return reactiveStore.getAll()
    }

    suspend fun fetch() {
        val response = service.categoryList()
        val entities = mapper.apply(response)
        reactiveStore.storeAll(entities)
    }
}