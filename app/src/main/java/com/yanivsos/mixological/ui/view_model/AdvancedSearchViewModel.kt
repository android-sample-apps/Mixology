package com.yanivsos.mixological.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yanivsos.mixological.analytics.AnalyticsDispatcher
import com.yanivsos.mixological.domain.GetIngredientsUseCase
import com.yanivsos.mixological.domain.GetSearchFiltersUseCase
import com.yanivsos.mixological.domain.MultipleFilterDrinkUseCase
import com.yanivsos.mixological.domain.models.DrinkFilter
import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.domain.models.FilterType
import com.yanivsos.mixological.domain.models.SearchFiltersModel
import com.yanivsos.mixological.search_autocomplete.DrinkAutoCompleteUiModel
import com.yanivsos.mixological.search_autocomplete.GetDrinkAutoCompleteUseCase
import com.yanivsos.mixological.search_autocomplete.toUiModel
import com.yanivsos.mixological.ui.models.DrinkFilterUiModel
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.models.SearchFiltersUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.function.Function

class AdvancedSearchViewModel(
    private val getIngredientsUseCase: GetIngredientsUseCase,
    private val getSearchFiltersUseCase: GetSearchFiltersUseCase,
    getAutoCompleteSuggestionsUseCase: GetDrinkAutoCompleteUseCase,
    private val filter: MultipleFilterDrinkUseCase,
    mapper: Function<List<DrinkPreviewModel>, List<DrinkPreviewUiModel>>,
    searchMapper: Function<SearchFiltersModel, SearchFiltersUiModel>
) : ViewModel() {

    val searchFiltersLiveData: LiveData<SearchFiltersUiModel> = getSearchFiltersUseCase
        .results
        .map { searchMapper.apply(it) }
        .combine(filter.selectedFilters) { searchUiModel: SearchFiltersUiModel,
                                           selectedFilters: Map<FilterType, Set<String>> ->

            val filtersMap = searchUiModel.filters.toMutableMap()
            selectedFilters.forEach { (filter, filterSet) ->
                val newSelectedFilters: List<DrinkFilterUiModel>? = filtersMap[filter]?.map {
                    val selected = filterSet.contains(it.name)//it.name == key
                    if (selected) {
                        Timber.d("selected filter: $filter, ${it.name}")
                    }
                    it.copy(selected = selected)
                }

                filtersMap[filter] = newSelectedFilters ?: emptyList()
            }
            SearchFiltersUiModel(
                filters = filtersMap,
                activeFilters = mapToSelectedFilters(selectedFilters)
            )
        }
        .flowOn(Dispatchers.IO)
        .asLiveData()

    val autoCompleteSuggestions: Flow<List<DrinkAutoCompleteUiModel>> =
        getAutoCompleteSuggestionsUseCase
            .suggestions
            .map { it.toUiModel() }
            .flowOn(Dispatchers.IO)

    private fun mapToSelectedFilters(selectedFilters: Map<FilterType, Set<String>>): Map<FilterType, Int?> {
        return selectedFilters.mapValues {
            val activeSize = it.value.size
            if (activeSize > 0) {
                activeSize
            } else {
                null
            }
        }
    }

    init {
        getIngredientsUseCase.refresh()
    }

    val resultsLiveData = filter
        .filterResults
        .map { mapper.apply(it) }
        .flowOn(Dispatchers.IO)
        .asLiveData()

    fun searchByName(name: String) {
        updateFilter(DrinkFilter(query = name, type = FilterType.NAME))
    }

    fun clearByName() {
        updateFilter(DrinkFilter(query = "", type = FilterType.NAME, active = false))
    }

    private fun clearOnGoingSearches() {
        clearByName()
        filter.cancel()
    }

    fun updateFilter(drinkFilter: DrinkFilter) {
        Timber.i("updating filter: $drinkFilter")
        viewModelScope.launch(Dispatchers.IO) {
            filter.updateFilter(drinkFilter)
            AnalyticsDispatcher.onSearchFilter(drinkFilter)
        }
    }

    fun onIngredientNameSearch(name: String?) {
        Timber.d("onIngredientNameSearch: $name")
        GlobalScope.launch(Dispatchers.IO) {
            getSearchFiltersUseCase
                .filterIngredientName(name)
        }
    }

    fun clearFilter(filterType: FilterType) {
        filter.clearFilter(filterType)
    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared")
        clearOnGoingSearches()
        getSearchFiltersUseCase.clear()
        getIngredientsUseCase.cancel()
    }
}