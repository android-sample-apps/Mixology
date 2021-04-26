package com.yanivsos.mixological.v2.search.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanivsos.mixological.database.DrinkPreviewModel
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.v2.drink.mappers.toUiModel
import com.yanivsos.mixological.v2.drink.repo.DrinkFilter
import com.yanivsos.mixological.v2.search.useCases.*
import com.yanivsos.mixological.v2.search.view.FilterChoiceGroupView
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SearchViewModel(
    getAutoCompleteSuggestionsUseCase: GetAutoCompleteSuggestionsUseCase,
    private val findSimilarIngredientsByNameUseCase: FindSimilarIngredientsByNameUseCase,
    private val fetchAndStoreFiltersUseCase: FetchAndStoreFiltersUseCase,
    private val searchDrinksUseCase: SearchDrinksUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    init {
        Timber.d("init hashcode ${hashCode()}")
        viewModelScope.launch {
            fetchAndStoreFiltersUseCase.fetchAndStore()
        }
    }

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
            }.onEach { mutableSearchKeyword = it.ingredientKeyword }


    private val filterErrorsMutableFlow = MutableSharedFlow<FilterError>()
    val filterErrors: Flow<FilterError> = filterErrorsMutableFlow

    val filterOperators: Flow<FilterOperators> = searchDrinksUseCase.filterOperators

    private var mutableSearchKeyword: String? = null
    val searchKeyword: String?
        get() {
            return mutableSearchKeyword
        }

    fun toggleFilter(drinkFilter: DrinkFilter) {
        viewModelScope.launch {
            runCatching {
                searchDrinksUseCase.toggleFilter(drinkFilter)
            }.onFailure { throwable ->
                Timber.e(throwable, "failed toggling filter $drinkFilter")
                showErrorToast(
                    FilterError.ToggleFilterError(
                        filter = drinkFilter,
                        throwable = throwable
                    )
                )
            }
        }
    }

    fun setIngredientsFilterChoice(filterChoice: FilterChoiceGroupView.SelectedFilterChoice) {
        viewModelScope.launch {
            val operator = when (filterChoice) {
                FilterChoiceGroupView.SelectedFilterChoice.And -> AccumulativeOperator.Intersection
                FilterChoiceGroupView.SelectedFilterChoice.Or -> AccumulativeOperator.Union
            }
            searchDrinksUseCase.setIngredientsOperator(operator)
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
            }.onFailure { throwable ->
                Timber.e(throwable, "Failed fetching by name: $name")
                showErrorToast(
                    FilterError.FetchByNameError(
                        name = name,
                        throwable = throwable
                    )
                )
            }
        }
    }

    fun clearByName() {
        viewModelScope.launch {
            searchDrinksUseCase.clearByName()
        }
    }

    private suspend fun showErrorToast(error: FilterError) {
        filterErrorsMutableFlow.emit(error)
    }

    private suspend fun mergeWithSimilarIngredients(
        selectedFilters: SelectedFilters,
        similarIngredientsState: SimilarIngredientsState
    ): SelectedFilters {
        return withContext(defaultDispatcher) {
            when (similarIngredientsState) {
                SimilarIngredientsState.All -> selectedFilters
                is SimilarIngredientsState.Found -> run {
                    val filteredResults =
                        selectedFilters.ingredients.filters.filter { it.name in similarIngredientsState.results }
                    val filteredIngredients = FilterCollection(
                        filters = filteredResults,
                        selectedCount = selectedFilters.ingredients.selectedCount
                    )
                    selectedFilters.copy(
                        ingredients = filteredIngredients,
                        ingredientKeyword = similarIngredientsState.keyword
                    )
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

sealed class FilterError {
    data class FetchByNameError(val name: String, val throwable: Throwable) : FilterError()
    data class ToggleFilterError(val filter: DrinkFilter, val throwable: Throwable) : FilterError()
}
