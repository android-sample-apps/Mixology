package com.zemingo.drinksmenu.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zemingo.drinksmenu.domain.GetSearchFiltersUseCase
import com.zemingo.drinksmenu.domain.MultipleFilterDrinkUseCase
import com.zemingo.drinksmenu.domain.models.DrinkFilter
import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.domain.models.FilterType
import com.zemingo.drinksmenu.domain.models.SearchFiltersModel
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import com.zemingo.drinksmenu.ui.models.SearchFiltersUiModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.util.function.Function

class AdvancedSearchViewModel(
    getSearchFiltersUseCase: GetSearchFiltersUseCase,
    private val filter: MultipleFilterDrinkUseCase,
    mapper: Function<List<DrinkPreviewModel>, List<DrinkPreviewUiModel>>,
    searchMapper: Function<SearchFiltersModel, SearchFiltersUiModel>
) : ViewModel() {

    //todo - combine to 1 use case
    val searchFilters: LiveData<SearchFiltersUiModel> = getSearchFiltersUseCase
        .results
        .map { searchMapper.apply(it) }
        .combine(filter.selectedFilters) { searchUiModel: SearchFiltersUiModel,
                                           selectedFilters: Map<FilterType, Set<String>> ->
            val filtersMap = searchUiModel.filters.toMutableMap()
            selectedFilters.forEach { (filter, filterSet) ->
                filtersMap[filter]?.forEach { filter ->
                    filter.selected = filterSet.contains(filter.name)//it.name == key
                    if (filter.selected) {
                        Timber.d("selected filter: $filter, ${filter.name}")
                    }
                }
            }
            SearchFiltersUiModel(
                filters = filtersMap,
                activeFilters = mapToSelectedFilters(selectedFilters)
            )
        }
        .asLiveData()

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

    val resultsLiveData = filter
        .filterResults
        .map { mapper.apply(it) }
        .asLiveData()

    fun searchByName(name: String) {
        filter.updateFilter(DrinkFilter(query = name, type = FilterType.NAME))
    }

    fun clearByName() {
        filter.updateFilter(DrinkFilter(query = "", type = FilterType.NAME, active = false))
    }

    private fun clearOnGoingSearches() {
        clearByName()
        filter.cancel()
    }

    fun updateFilter(drinkFilter: DrinkFilter) {
        Timber.i("updating filter: $drinkFilter")
        filter.updateFilter(drinkFilter)
    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared")
        clearOnGoingSearches()
    }
}