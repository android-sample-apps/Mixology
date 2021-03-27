package com.yanivsos.mixological.v2.search.useCases

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.v2.drink.mappers.toPreviewModel
import com.yanivsos.mixological.v2.drink.repo.DrinkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class FetchDrinkByNameUseCase(
    private val drinkRepository: DrinkRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    private val fetchDrinkStateFlow = MutableStateFlow<FetchDrinkState>(FetchDrinkState.NotSelected)
    val fetchDrinkState: Flow<FetchDrinkState> = fetchDrinkStateFlow

    suspend fun fetchByName(name: String) {
        withContext(defaultDispatcher) {
            runCatching {
                if (name.isBlank()) {
                    fetchDrinkStateFlow.value = FetchDrinkState.NotSelected
                    return@runCatching
                }

                val results = drinkRepository.fetchByName(name)
                if (results.isEmpty()) {
                    FetchDrinkState.NoResults
                } else {
                    FetchDrinkState.FoundResults(results.map { it.toPreviewModel() })
                }.also {
                    fetchDrinkStateFlow.value = it
                }
            }
        }
    }

    fun clear() {
        fetchDrinkStateFlow.value = FetchDrinkState.NotSelected
    }
}

sealed class FetchDrinkState {
    object NoResults : FetchDrinkState()
    object NotSelected : FetchDrinkState()
    data class FoundResults(val drinks: List<DrinkPreviewModel>) : FetchDrinkState()
}
