package com.zemingo.drinksmenu.repo.repositories

import com.zemingo.drinksmenu.domain.models.GlassModel
import com.zemingo.drinksmenu.repo.DrinkService
import com.zemingo.drinksmenu.repo.models.DrinksWrapperResponse
import com.zemingo.drinksmenu.repo.models.GlassResponse
import com.zemingo.drinksmenu.repo.reactive_store.ReactiveStore
import kotlinx.coroutines.flow.Flow
import java.util.function.Function

class GlassRepository(
    private val reactiveStore: ReactiveStore<String, GlassModel, Void>,
    private val service: DrinkService,
    private val mapper: Function<DrinksWrapperResponse<GlassResponse>, List<GlassModel>>
) {

    fun getAll(): Flow<List<GlassModel>> {
        return reactiveStore.getAll()
    }

    suspend fun fetchAll() {
        val response = service.getGlassList()
        val glasses = mapper.apply(response)
        reactiveStore.storeAll(glasses)
    }
}