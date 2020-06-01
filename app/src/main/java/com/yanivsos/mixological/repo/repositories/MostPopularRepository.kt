package com.yanivsos.mixological.repo.repositories

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.domain.models.MostPopularModel
import com.yanivsos.mixological.repo.DrinkService
import com.yanivsos.mixological.repo.models.DrinkPreviewResponse
import com.yanivsos.mixological.repo.models.DrinksWrapperResponse
import com.yanivsos.mixological.repo.reactiveStore.RemoveAllReactiveStore
import kotlinx.coroutines.flow.Flow
import java.util.function.Function

class MostPopularRepository(
    private val service: DrinkService,
    private val reactiveStore: RemoveAllReactiveStore<String, MostPopularModel, Unit>,
    private val mapper: Function<DrinksWrapperResponse<DrinkPreviewResponse>, List<DrinkPreviewModel>>
) {

    fun getAll(): Flow<List<MostPopularModel>> {
        return reactiveStore.get(Unit)
    }

    fun storeAll(mostPopular: List<MostPopularModel>)  {
        reactiveStore.storeAll(mostPopular)
    }

    fun removeAll() {
        reactiveStore.removeAll()
    }

    suspend fun fetchMostPopulars(): List<DrinkPreviewModel> {
        val response = service.getMostPopular()
        return mapper.apply(response)
    }
}