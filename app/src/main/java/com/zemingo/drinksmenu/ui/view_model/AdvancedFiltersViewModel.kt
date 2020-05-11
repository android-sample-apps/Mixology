package com.zemingo.drinksmenu.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zemingo.drinksmenu.domain.GetSearchFiltersUseCase
import com.zemingo.drinksmenu.domain.models.SearchFiltersModel
import com.zemingo.drinksmenu.ui.models.SearchFiltersUiModel
import kotlinx.coroutines.flow.map
import java.util.function.Function

class AdvancedFiltersViewModel(
    getSearchFiltersUseCase: GetSearchFiltersUseCase,
    mapper: Function<SearchFiltersModel, SearchFiltersUiModel>
) : ViewModel() {

    val searchFilters = getSearchFiltersUseCase
        .results
        .map { mapper.apply(it) }
        .asLiveData()
}