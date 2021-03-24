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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
    getAutoCompleteSuggestionsUseCase: GetAutoCompleteSuggestionsUseCase,
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

    val selectedFilters = searchDrinksUseCase.filters

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

    private suspend fun mapToFilterBadgeState(selectedFilters: SelectedFilters): FilterBadgeState {
        return withContext(defaultDispatcher) {
            var count = 0
            count += selectedFilters.alcoholic.countSelected()
            count += selectedFilters.glasses.countSelected()
            count += selectedFilters.categories.countSelected()
            count += selectedFilters.ingredients.countSelected()
            if (count == 0) {
                FilterBadgeState.None
            } else {
                FilterBadgeState.Active(count)
            }
        }
    }

    private fun List<FilterModel>.countSelected() = count { it.isSelected }

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
