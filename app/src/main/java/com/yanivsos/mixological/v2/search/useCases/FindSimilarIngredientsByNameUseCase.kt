package com.yanivsos.mixological.v2.search.useCases

import com.yanivsos.mixological.database.IngredientModel
import com.yanivsos.mixological.v2.search.repo.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber

class FindSimilarIngredientsByNameUseCase(
    private val searchRepository: SearchRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    private val mutex = Mutex()
    private val similarIngredientsFlow =
        MutableStateFlow<SimilarIngredientsState>(SimilarIngredientsState.All)

    val similarIngredients: Flow<SimilarIngredientsState> = similarIngredientsFlow

    suspend fun updateName(name: String?) {
        withContext(defaultDispatcher) {
            mutex.withLock { findSimilar(name) }
        }
    }

    private suspend fun findSimilar(name: String?) {
        Timber.d("finding similar ingredients to name[$name]")
        if (name.isNullOrBlank()) {
            similarIngredientsFlow.value = SimilarIngredientsState.All
        } else {
            val results = searchRepository.findIngredientsBySimilarName(name)
            similarIngredientsFlow.value = SimilarIngredientsState.Found(mapToFilterModel(results), name)
        }
    }

    private fun mapToFilterModel(results: List<IngredientModel>): Set<String> {
        return results.map { it.name }.toSet()
    }
}


sealed class SimilarIngredientsState {
    object All : SimilarIngredientsState()
    data class Found(val results: Set<String>, val keyword: String?) : SimilarIngredientsState()
}
