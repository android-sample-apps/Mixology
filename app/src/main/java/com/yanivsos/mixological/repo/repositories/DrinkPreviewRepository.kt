package com.yanivsos.mixological.repo.repositories

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.repo.DrinkService
import com.yanivsos.mixological.repo.models.DrinkPreviewResponse
import com.yanivsos.mixological.repo.models.DrinkResponse
import com.yanivsos.mixological.repo.models.DrinksWrapperResponse
import com.yanivsos.mixological.repo.models.NullableDrinksWrapperResponse
import com.yanivsos.mixological.repo.reactiveStore.DrinkPreviewParam
import com.yanivsos.mixological.repo.reactiveStore.NonRemovableReactiveStore
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.util.function.Function

class DrinkPreviewRepository(
    private val service: DrinkService,
    private val reactiveStore: NonRemovableReactiveStore<DrinkPreviewModel, DrinkPreviewParam>,
    private val mapper: Function<DrinksWrapperResponse<DrinkPreviewResponse>, List<DrinkPreviewModel>>,
    private val searchMapper: Function<NullableDrinksWrapperResponse<DrinkResponse>, List<DrinkPreviewModel>>
) {

    fun getAll(): Flow<List<DrinkPreviewModel>> {
        return reactiveStore.get(DrinkPreviewParam.All)
    }

    fun getByIds(ids: List<String>): Flow<List<DrinkPreviewModel>> {
        return reactiveStore.get(DrinkPreviewParam.ByIds(ids))
    }

    fun storeAll(drinkPreviews: List<DrinkPreviewModel>) {
        reactiveStore.storeAll(drinkPreviews)
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