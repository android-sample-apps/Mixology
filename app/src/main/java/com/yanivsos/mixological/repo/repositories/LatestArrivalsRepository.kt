package com.yanivsos.mixological.repo.repositories

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.domain.models.LatestArrivalsModel
import com.yanivsos.mixological.repo.DrinkService
import com.yanivsos.mixological.repo.models.DrinkPreviewResponse
import com.yanivsos.mixological.repo.models.DrinksWrapperResponse
import com.yanivsos.mixological.repo.reactiveStore.ReplaceAllReactiveStore
import kotlinx.coroutines.flow.Flow
import java.util.function.Function

class LatestArrivalsRepository(
    private val service: DrinkService,
    private val reactiveStore: ReplaceAllReactiveStore<String, LatestArrivalsModel, Unit>,
    private val mapper: Function<DrinksWrapperResponse<DrinkPreviewResponse>, List<DrinkPreviewModel>>
) {

    fun getAll(): Flow<List<LatestArrivalsModel>> {
        return reactiveStore.get(Unit)
    }

    fun replaceAll(data: List<LatestArrivalsModel>) {
        reactiveStore.replaceAll(data)
    }

    suspend fun fetchLatestArrivals(): List<DrinkPreviewModel> {
        val response = service.getLatestArrivals()
        return mapper.apply(response)
    }
}