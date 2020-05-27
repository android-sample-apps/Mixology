package com.yanivsos.mixological.repo.repositories

import com.yanivsos.mixological.domain.models.AlcoholicFilterModel
import com.yanivsos.mixological.repo.DrinkService
import com.yanivsos.mixological.repo.models.AlcoholicFilterResponse
import com.yanivsos.mixological.repo.models.DrinksWrapperResponse
import com.yanivsos.mixological.repo.reactiveStore.ReactiveStore
import kotlinx.coroutines.flow.Flow
import java.util.function.Function

class AlcoholicFilterRepository(
    private val reactiveStore: ReactiveStore<String, AlcoholicFilterModel, Void>,
    private val service: DrinkService,
    private val mapper: Function<DrinksWrapperResponse<AlcoholicFilterResponse>, List<AlcoholicFilterModel>>
) {

    fun getAll(): Flow<List<AlcoholicFilterModel>> {
        return reactiveStore.getAll()
    }

    suspend fun fetchAll() {
        val response = service.getAlcoholicFilterList()
        val data = mapper.apply(response)
        reactiveStore.storeAll(data)
    }
}