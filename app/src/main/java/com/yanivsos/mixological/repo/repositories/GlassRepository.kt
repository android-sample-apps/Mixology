package com.yanivsos.mixological.repo.repositories

import com.yanivsos.mixological.domain.models.GlassModel
import com.yanivsos.mixological.repo.DrinkService
import com.yanivsos.mixological.repo.models.DrinksWrapperResponse
import com.yanivsos.mixological.repo.models.GlassResponse
import com.yanivsos.mixological.repo.reactiveStore.NonRemovableReactiveStore
import kotlinx.coroutines.flow.Flow
import java.util.function.Function

class GlassRepository(
    private val reactiveStore: NonRemovableReactiveStore<GlassModel, Unit>,
    private val service: DrinkService,
    private val mapper: Function<DrinksWrapperResponse<GlassResponse>, List<GlassModel>>
) {

    fun getAll(): Flow<List<GlassModel>> {
        return reactiveStore.get(Unit)
    }

    suspend fun fetchAll() {
        val response = service.getGlassList()
        val glasses = mapper.apply(response)
        reactiveStore.storeAll(glasses)
    }
}