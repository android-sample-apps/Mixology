package com.zemingo.drinksmenu.repo.repositories

import com.zemingo.drinksmenu.domain.models.CategoryModel
import com.zemingo.drinksmenu.repo.models.CategoryResponse
import com.zemingo.drinksmenu.repo.models.DrinksWrapperResponse
import com.zemingo.drinksmenu.repo.DrinkService
import com.zemingo.drinksmenu.repo.reactiveStore.ReactiveStore
import kotlinx.coroutines.flow.Flow
import java.util.function.Function

class CategoryRepository(
    private val service: DrinkService,
    private val reactiveStore: ReactiveStore<String, CategoryModel, Void>,
    private val mapper: Function<DrinksWrapperResponse<CategoryResponse>, List<CategoryModel>>
) {
    fun get(): Flow<List<CategoryModel>> {
        return reactiveStore.getAll()
    }

    suspend fun fetch() {
        val response = service.getCategoryList()
        val entities = mapper.apply(response)
        reactiveStore.storeAll(entities)
    }
}