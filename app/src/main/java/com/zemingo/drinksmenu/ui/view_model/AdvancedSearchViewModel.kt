package com.zemingo.drinksmenu.ui.view_model

import androidx.lifecycle.*
import com.zemingo.drinksmenu.domain.AdvancedSearchUseCase
import com.zemingo.drinksmenu.domain.MultipleFilterDrinkUseCase
import com.zemingo.drinksmenu.domain.models.DrinkFilter
import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.function.Function

class AdvancedSearchViewModel(
    private val searchUseCase: AdvancedSearchUseCase,
    private val filter: MultipleFilterDrinkUseCase,
    mapper: Function<List<DrinkPreviewModel>, List<DrinkPreviewUiModel>>
) : ViewModel() {

    val resultsLiveData = filter
        .filterResults
        .map { mapper.apply(it) }
        .asLiveData()

    fun searchByName(name: String) {
        searchUseCase.search(name)
    }

    fun clearOnGoingSearches() {
        searchUseCase.clearOnGoingSearch()
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