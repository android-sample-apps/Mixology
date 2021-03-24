package com.yanivsos.mixological.v2.search.useCases

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.v2.drink.repo.DrinkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetAutoCompleteSuggestionsUseCase(
    drinkRepository: DrinkRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    val suggestions: Flow<List<AutoCompleteSuggestionModel>> =
        drinkRepository
            .getAllPreviews()
            .map { mapToSuggestions(it) }


    private suspend fun mapToSuggestions(previews: List<DrinkPreviewModel>): List<AutoCompleteSuggestionModel> {
        return withContext(defaultDispatcher) {
            previews.map { AutoCompleteSuggestionModel(it.name) }
        }
    }
}

data class AutoCompleteSuggestionModel(val name: String)
