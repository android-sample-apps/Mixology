package com.yanivsos.mixological.v2.search.useCases

interface FetchAndStoreUseCase {
    suspend fun fetchAndStore()
    val name: String
}
