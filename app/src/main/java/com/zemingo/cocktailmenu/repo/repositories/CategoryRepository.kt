package com.zemingo.cocktailmenu.repo.repositories

import com.zemingo.cocktailmenu.models.CategoryEntity
import com.zemingo.cocktailmenu.models.CategoryResponse
import com.zemingo.cocktailmenu.models.DrinksWrapperResponse
import com.zemingo.cocktailmenu.repo.CocktailService
import com.zemingo.cocktailmenu.repo.reactive_store.ReactiveStore
import kotlinx.coroutines.flow.Flow
import java.util.function.Function

class CategoryRepository(
    private val service: CocktailService,
    private val reactiveStore: ReactiveStore<CategoryEntity>,
    private val mapper: Function<DrinksWrapperResponse<CategoryResponse>, List<CategoryEntity>>
) {

    fun get(): Flow<List<CategoryEntity>> {
        return reactiveStore.getAll()
    }

    suspend fun fetch() {
        val response = service.categoryList()
        val entities = mapper.apply(response)
        reactiveStore.storeAll(entities)
    }
}