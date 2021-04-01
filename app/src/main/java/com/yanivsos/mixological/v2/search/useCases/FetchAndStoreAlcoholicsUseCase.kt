package com.yanivsos.mixological.v2.search.useCases

import com.yanivsos.mixological.v2.search.repo.SearchRepository

class FetchAndStoreAlcoholicsUseCase(
    private val searchRepository: SearchRepository
) : FetchAndStoreUseCase {

    override suspend fun fetchAndStore() {
        searchRepository
            .fetchAlcoholicsList()
            .also { searchRepository.storeAlcoholics(it) }
    }

    override val name: String = "Alcoholics"
}
