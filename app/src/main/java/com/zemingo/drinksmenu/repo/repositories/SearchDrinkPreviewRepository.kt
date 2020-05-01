package com.zemingo.drinksmenu.repo.repositories

import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.domain.models.PreviousSearchModel
import com.zemingo.drinksmenu.repo.reactive_store.DrinkPreviewParam
import com.zemingo.drinksmenu.repo.reactive_store.ReactiveStore
import kotlinx.coroutines.flow.Flow

class SearchDrinkPreviewRepository(
    private val searchReactiveStore: ReactiveStore<String, PreviousSearchModel, Void>,
    private val drinkReactiveStore: ReactiveStore<String, DrinkPreviewModel, DrinkPreviewParam>
) {

    fun getHistory(): Flow<List<DrinkPreviewModel>> {
        return drinkReactiveStore
            .getByParam(DrinkPreviewParam.SearchHistory)
    }

    fun storeAll(previousSearches: List<PreviousSearchModel>) {
        searchReactiveStore.storeAll(previousSearches)
    }


}