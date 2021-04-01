package com.yanivsos.mixological.v2.search.useCases

import com.yanivsos.mixological.v2.search.repo.SearchRepository

class FetchAndStoreGlassesUseCase(
    private val searchRepository: SearchRepository
): FetchAndStoreUseCase {

    override suspend fun fetchAndStore() {
        searchRepository
            .fetchGlassesList()
            .also { searchRepository.storeGlasses(it) }
    }

    override val name: String = "Glasses"
}
