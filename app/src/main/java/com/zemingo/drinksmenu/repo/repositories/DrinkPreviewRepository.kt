package com.zemingo.drinksmenu.repo.repositories

import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.repo.DrinkService
import com.zemingo.drinksmenu.repo.models.DrinkPreviewResponse
import com.zemingo.drinksmenu.repo.models.DrinkResponse
import com.zemingo.drinksmenu.repo.models.DrinksWrapperResponse
import com.zemingo.drinksmenu.repo.models.NullableDrinksWrapperResponse
import com.zemingo.drinksmenu.repo.reactiveStore.DrinkPreviewParam
import com.zemingo.drinksmenu.repo.reactiveStore.ReactiveStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.util.function.Function

class DrinkPreviewRepository(
    private val service: DrinkService,
    private val reactiveStore: ReactiveStore<String, DrinkPreviewModel, DrinkPreviewParam>,
    private val mapper: Function<DrinksWrapperResponse<DrinkPreviewResponse>, List<DrinkPreviewModel>>,
    private val searchMapper: Function<NullableDrinksWrapperResponse<DrinkResponse>, List<DrinkPreviewModel>>
) {

    fun getAll(): Flow<List<DrinkPreviewModel>> {
        return reactiveStore.getAll()
    }

    fun getByIds(ids: List<String>): Flow<List<DrinkPreviewModel>> {
        return reactiveStore.getAll(ids)
    }

    fun storeAll(drinkPreviews: List<DrinkPreviewModel>) {
        reactiveStore.storeAll(drinkPreviews)
    }

    fun mostPopular(): Flow<List<DrinkPreviewModel>> {
        //todo - replace with real API
        return reactiveStore.getAll().map { it.take(5) }
    }

    fun latestArrivals(): Flow<List<DrinkPreviewModel>> {
        //todo - replace with real API
        return reactiveStore.getAll().map { it.takeLast(5) }
    }

    suspend fun fetchByCategoryImmediate(category: String): List<DrinkPreviewModel> {
        val response = service.filterByCategory(category.replace(" ", "_"))
        return mapper.apply(response).apply { storeAll(this) }
    }

    suspend fun fetchByLetter(letter: String) {
        try {
            val response = service.searchByFirstLetter(letter)
            searchMapper.apply(response).run { storeAll(this) }
        } catch (e: Exception) {
            Timber.e(e, "Unable to fetch by letter $letter")
        }
    }

}