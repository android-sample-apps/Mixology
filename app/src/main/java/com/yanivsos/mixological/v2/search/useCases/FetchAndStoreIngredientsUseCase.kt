package com.yanivsos.mixological.v2.search.useCases

import com.yanivsos.mixological.v2.search.repo.SearchRepository

class FetchAndStoreIngredientsUseCase(
    private val searchRepository: SearchRepository
): FetchAndStoreUseCase {

    override suspend fun fetchAndStore() {
        searchRepository
            .fetchIngredientsList()
            .also { searchRepository.storeIngredients(it) }
    }

    override val name: String = "Ingredients"
}
