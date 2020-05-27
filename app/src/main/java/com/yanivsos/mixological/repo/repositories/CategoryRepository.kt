package com.yanivsos.mixological.repo.repositories

import com.yanivsos.mixological.domain.models.CategoryModel
import com.yanivsos.mixological.repo.models.CategoryResponse
import com.yanivsos.mixological.repo.models.DrinksWrapperResponse
import com.yanivsos.mixological.repo.DrinkService
import com.yanivsos.mixological.repo.reactiveStore.ReactiveStore
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