package com.zemingo.drinksmenu.repo.repositories

import com.zemingo.drinksmenu.domain.models.AlcoholicFilterModel
import com.zemingo.drinksmenu.domain.models.GlassModel
import com.zemingo.drinksmenu.repo.DrinkService
import com.zemingo.drinksmenu.repo.models.AlcoholicFilterResponse
import com.zemingo.drinksmenu.repo.models.DrinksWrapperResponse
import com.zemingo.drinksmenu.repo.models.GlassResponse
import com.zemingo.drinksmenu.repo.reactive_store.ReactiveStore
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