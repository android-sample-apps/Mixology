package com.yanivsos.mixological.v2.search.useCases

import com.yanivsos.mixological.v2.search.repo.SearchRepository

class FetchAndStoreCategoriesUseCase(
    private val searchRepository: SearchRepository
): FetchAndStoreUseCase {

    override suspend fun fetchAndStore() {
        searchRepository
            .fetchCategoriesList()
            .also { searchRepository.storeCategories(it) }
    }

    override val name: String = "Categories"
}
