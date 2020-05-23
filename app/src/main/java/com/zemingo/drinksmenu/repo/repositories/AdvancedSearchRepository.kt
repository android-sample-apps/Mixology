package com.zemingo.drinksmenu.repo.repositories

import com.zemingo.drinksmenu.domain.models.DrinkFilter
import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.domain.models.FilterType
import com.zemingo.drinksmenu.repo.DrinkService
import com.zemingo.drinksmenu.repo.models.DrinkPreviewResponse
import com.zemingo.drinksmenu.repo.models.DrinkResponse
import com.zemingo.drinksmenu.repo.models.DrinksWrapperResponse
import com.zemingo.drinksmenu.repo.models.NullableDrinksWrapperResponse
import com.zemingo.drinksmenu.repo.reactiveStore.DrinkReactiveStore
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