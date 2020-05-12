package com.zemingo.drinksmenu.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zemingo.drinksmenu.domain.GetSearchFiltersUseCase
import com.zemingo.drinksmenu.domain.MultipleFilterDrinkUseCase
import com.zemingo.drinksmenu.domain.models.DrinkFilter
import com.zemingo.drinksmenu.domain.models.FilterType
import com.zemingo.drinksmenu.domain.models.SearchFiltersModel
import com.zemingo.drinksmenu.ui.models.SearchFiltersUiModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.util.function.Function

class AdvancedFiltersViewModel(
    getSearchFiltersUseCase: GetSearchFiltersUseCase,
    getMultipleFilterDrinkUseCase: MultipleFilterDrinkUseCase,
    mapper: Function<SearchFiltersModel, SearchFiltersUiModel>
) : ViewModel() {

    //todo - combine to 1 use case
    val searchFilters: LiveData<SearchFiltersUiModel> = getSearchFiltersUseCase
        .results
        .map { mapper.apply(it) }
        .combine(getMultipleFilterDrinkUseCase.selectedFilters) { searchUiModel: SearchFiltersUiModel, selectedFilters: Map<FilterType, String> ->
            val filtersMap = searchUiModel.filters.toMutableMap()
            selectedFilters.forEach { (filter, key) ->
                filtersMap[filter]?.forEach {
                    it.selected = it.name == key
                }
            }
            SearchFiltersUiModel(
                filtersMap
            )
        }
        .asLiveData()
}