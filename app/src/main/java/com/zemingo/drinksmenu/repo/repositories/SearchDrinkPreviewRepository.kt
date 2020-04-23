package com.zemingo.drinksmenu.repo.repositories

import com.zemingo.drinksmenu.models.DrinkPreviewModel
import com.zemingo.drinksmenu.models.PreviousSearchModel
import com.zemingo.drinksmenu.repo.reactive_store.ReactiveStore
import kotlinx.coroutines.flow.Flow

class SearchDrinkPreviewRepository(
    private val searchReactiveStore: ReactiveStore<PreviousSearchModel>,
    private val previewReactiveStore: ReactiveStore<DrinkPreviewModel>
) {

    fun getAll(): Flow<List<PreviousSearchModel>> {
        return searchReactiveStore.getAll()
    }

    fun storeAll(previousSearches: List<PreviousSearchModel>) {
        searchReactiveStore.storeAll(previousSearches)
    }

    fun getHistory(): Flow<List<DrinkPreviewModel>> {
        searchReactiveStore

        return previewReactiveStore.getAll()
    }
}