package com.zemingo.drinksmenu.repo.repositories

import com.zemingo.drinksmenu.models.DrinkPreviewModel
import com.zemingo.drinksmenu.models.PreviousSearchModel
import com.zemingo.drinksmenu.repo.reactive_store.SearchDrinkPreviewReactiveStore
import kotlinx.coroutines.flow.Flow

class SearchDrinkPreviewRepository(
    private val reactiveStore: SearchDrinkPreviewReactiveStore
) {

    fun getAll(): Flow<List<DrinkPreviewModel>> {
        return reactiveStore.getAllWithDrinks()
    }

    fun storeAll(previousSearches: List<PreviousSearchModel>) {
        reactiveStore.storeAll(previousSearches)
    }
}