package com.yanivsos.mixological.v2.search.useCases

import timber.log.Timber

class FetchAndStoreFiltersUseCase(
    private val fetchAndStoreAlcoholicsUseCase: FetchAndStoreAlcoholicsUseCase,
    private val fetchAndStoreCategoriesUseCase: FetchAndStoreCategoriesUseCase,
    private val fetchAndStoreGlassesUseCase: FetchAndStoreGlassesUseCase,
    private val fetchAndStoreIngredientsUseCase: FetchAndStoreIngredientsUseCase
) {

    suspend fun fetchAndStore() {
        fetchAndStore(fetchAndStoreAlcoholicsUseCase)
        fetchAndStore(fetchAndStoreCategoriesUseCase)
        fetchAndStore(fetchAndStoreGlassesUseCase)
        fetchAndStore(fetchAndStoreIngredientsUseCase)
    }

    private suspend fun fetchAndStore(fetchAndStoreUseCase: FetchAndStoreUseCase) {
        runCatching {
            fetchAndStoreUseCase.fetchAndStore()
        }.onFailure { Timber.e("Failed fetching and storing from ${fetchAndStoreUseCase.name}") }
    }
}
