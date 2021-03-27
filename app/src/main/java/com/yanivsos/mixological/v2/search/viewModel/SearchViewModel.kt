package com.yanivsos.mixological.v2.search.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.v2.drink.mappers.toUiModel
import com.yanivsos.mixological.v2.drink.repo.DrinkFilter
import com.yanivsos.mixological.v2.search.useCases.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SearchViewModel(
    getAutoCompleteSuggestionsUseCase: GetAutoCompleteSuggestionsUseCase,
    private val findSimilarIngredientsByNameUseCase: FindSimilarIngredientsByNameUseCase,
    private val searchDrinksUseCase: SearchDrinksUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    val previewsState: Flow<PreviewState> =
        searchDrinksUseCase
            .previews
            .map { mapToPreviewStats(it) }
            .stateIn(viewModelScope, SharingStarted.Lazily, PreviewState.Loading)

    val suggestions: Flow<AutoCompleteSuggestionsState> =
        getAutoCompleteSuggestionsUseCase
            .suggestions
            .map { mapToState(it) }
            .stateIn(viewModelScope, SharingStarted.Lazily, AutoCompleteSuggestionsState.Empty)

    val filterBadge: Flow<FilterBadgeState> =
        searchDrinksUseCase
            .filters
            .map { mapToFilterBadgeState(it) }

    val selectedFilters: Flow<SelectedFilters> =
        searchDrinksUseCase
            .filters
            .combine(findSimilarIngredientsByNameUseCase.similarIngredients) { selectedFilters, similarIngredientsState ->
                mergeWithSimilarIngredients(selectedFilters, similarIngredientsState)
            }

    fun toggleFilter(drinkFilter: DrinkFilter) {
        viewModelScope.launch {
            searchDrinksUseCase.toggleFilter(drinkFilter)
        }
    }

    fun clearAlcoholicFilter() {
        viewModelScope.launch {
            searchDrinksUseCase.clearAlcoholicFilter()
        }
    }

    fun clearGlassesFilter() {
        viewModelScope.launch {
            searchDrinksUseCase.clearGlassesFilter()
        }
    }

    fun clearCategoriesFilter() {
        viewModelScope.launch {
            searchDrinksUseCase.clearCategoriesFilter()
        }
    }

    fun clearIngredientsFilter() {
        viewModelScope.launch {
            searchDrinksUseCase.clearIngredientsFilter()
        }
    }

    fun findRelevantIngredients(name: String?) {
        viewModelScope.launch {
            findSimilarIngredientsByNameUseCase.updateName(name)
        }
    }

    fun fetchByName(name: String) {
        viewModelScope.launch {
            runCatching {
                searchDrinksUseCase.fetchByName(name)
            }.onFailure { Timber.e(it, "Failed fetching by name: $name") }
        }
    }

    fun clearByName() {
        viewModelScope.launch {
            searchDrinksUseCase.clearByName()
        }
    }

    private suspend fun mergeWithSimilarIngredients(
        selectedFilters: SelectedFilters,
        similarIngredientsState: SimilarIngredientsState
    ): SelectedFilters {
        return withContext(defaultDispatcher) {
            when (similarIngredientsState) {
                SimilarIngredientsState.All -> selectedFilters
                is SimilarIngredientsState.Found -> run {
                    val filteredIngredients = FilterCollection(
                        filters = similarIngredientsState.results,
                        selectedCount = selectedFilters.ingredients.selectedCount
                    )
                    selectedFilters.copy(ingredients = filteredIngredients)
                }
            }

        }
    }

    private suspend fun mapToFilterBadgeState(selectedFilters: SelectedFilters): FilterBadgeState {
        return withContext(defaultDispatcher) {
            var count = 0
            count += selectedFilters.alcoholic.selectedCount
            count += selectedFilters.glasses.selectedCount
            count += selectedFilters.categories.selectedCount
            count += selectedFilters.ingredients.selectedCount
            if (count == 0) {
                FilterBadgeState.None
            } else {
                FilterBadgeState.Active(count)
            }
        }
    }

    private suspend fun mapToPreviewStats(list: List<DrinkPreviewModel>): PreviewState {
        return withContext(defaultDispatcher) {
            if (list.isEmpty()) {
                PreviewState.NoResults
            } else {
                PreviewState.Results(list.toUiModel())
            }
        }
    }

    private suspend fun mapToState(list: List<AutoCompleteSuggestionModel>): AutoCompleteSuggestionsState {
        return when {
            list.isEmpty() -> AutoCompleteSuggestionsState.Empty
            else -> AutoCompleteSuggestionsState.Found(mapToSuggestions(list))
        }
    }

    private suspend fun mapToSuggestions(list: List<AutoCompleteSuggestionModel>): List<String> {
        return withContext(defaultDispatcher) {
            list.map { it.name }
        }
    }
}

sealed class PreviewState {
    data class Results(val previews: List<DrinkPreviewUiModel>) : PreviewState()
    object NoResults : PreviewState()
    object Loading : PreviewState()
}

sealed class AutoCompleteSuggestionsState {
    object Empty : AutoCompleteSuggestionsState()
    data class Found(val suggestions: List<String>) : AutoCompleteSuggestionsState()
}

sealed class FilterBadgeState {
    object None : FilterBadgeState()
    data class Active(val count: Int) : FilterBadgeState()
}