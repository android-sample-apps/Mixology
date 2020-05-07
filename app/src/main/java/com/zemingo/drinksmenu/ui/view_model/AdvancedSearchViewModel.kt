package com.zemingo.drinksmenu.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zemingo.drinksmenu.domain.AdvancedSearchUseCase
import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import kotlinx.coroutines.flow.map
import java.util.function.Function

class AdvancedSearchViewModel(
    private val searchUseCase: AdvancedSearchUseCase,
    mapper: Function<List<DrinkPreviewModel>, List<DrinkPreviewUiModel>>
) : ViewModel() {

    val resultsLiveData: LiveData<List<DrinkPreviewUiModel>> = searchUseCase
        .searchResults
        .map { mapper.apply(it) }
        .asLiveData(viewModelScope.coroutineContext)

    fun searchByName(name: String) {
        searchUseCase.search(name)
    }

    fun clearOnGoingSearches() {
        searchUseCase.clearOnGoingSearch()
    }

    override fun onCleared() {
        super.onCleared()
        clearOnGoingSearches()
    }
}