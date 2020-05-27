package com.yanivsos.mixological.repo.repositories

import com.yanivsos.mixological.domain.models.DrinkFilter
import com.yanivsos.mixological.domain.models.DrinkModel
import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.domain.models.FilterType
import com.yanivsos.mixological.repo.DrinkService
import com.yanivsos.mixological.repo.models.DrinkPreviewResponse
import com.yanivsos.mixological.repo.models.DrinkResponse
import com.yanivsos.mixological.repo.models.DrinksWrapperResponse
import com.yanivsos.mixological.repo.models.NullableDrinksWrapperResponse
import com.yanivsos.mixological.repo.reactiveStore.DrinkReactiveStore
import timber.log.Timber
import java.util.function.Function

class AdvancedSearchRepository(
    private val service: DrinkService,
    private val drinkReactiveStore: DrinkReactiveStore,
    private val drinkMapper: Function<NullableDrinksWrapperResponse<DrinkResponse>, List<DrinkModel>>,
    private val previewMapper: Function<DrinksWrapperResponse<DrinkPreviewResponse>, List<DrinkPreviewModel>>
) {

    private suspend fun fetchByName(name: String): List<DrinkModel> {
        val response = service.searchByName(name)
        return drinkMapper.apply(response).apply {
            drinkReactiveStore.storeAll(this)
        }
    }

    private suspend fun filterByName(name: String): List<DrinkPreviewModel> {
        return fetchByName(name).map { DrinkPreviewModel(it) }
    }

    /*suspend fun filter(filter: DrinkFilter): List<DrinkPreviewModel> {
        return filterBy(filter).apply {
            previewReactiveStore.storeAll(this)
        }
    }*/

    suspend fun filterBy(filter: DrinkFilter): List<DrinkPreviewModel> {
        try {
            val response: DrinksWrapperResponse<DrinkPreviewResponse> = when (filter.type) {
                FilterType.ALCOHOL -> service.filterByAlcoholic(filter.query)
                FilterType.CATEGORY -> service.filterByCategory(filter.query)
                FilterType.GLASS -> service.filterByGlass(filter.query)
                FilterType.INGREDIENTS -> service.filterByIngredient(filter.query)
                FilterType.NAME -> return filterByName(filter.query)
            }
            return previewMapper.apply(response)
        }
        catch (e: Exception) {
            Timber.e(e, "Failed to filterBy $filter")
            return emptyList()
        }
    }
}