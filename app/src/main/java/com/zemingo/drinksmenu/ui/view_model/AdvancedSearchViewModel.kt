package com.zemingo.drinksmenu.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zemingo.drinksmenu.domain.AdvancedSearchUseCase
import com.zemingo.drinksmenu.domain.MultipleFilterDrinkUseCase
import com.zemingo.drinksmenu.domain.models.DrinkFilter
import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.util.function.Function

class AdvancedSearchViewModel(
    private val searchUseCase: AdvancedSearchUseCase,
    private val filter: MultipleFilterDrinkUseCase,
    mapper: Function<List<DrinkPreviewModel>, List<DrinkPreviewUiModel>>
) : ViewModel() {

    val resultsLiveData: LiveData<List<DrinkPreviewUiModel>> = searchUseCase
        .searchResults
        .combine(filter.filterResults) { results: List<DrinkPreviewModel>,
                                         filter: List<DrinkPreviewModel>? ->

            Timber.d("results: ${results.map { it.id }}")
            Timber.d("filter: ${filter?.map { it.id }}")
            val intersection = filter?.intersect(results) ?: results
            intersection.apply {
                Timber.i("received filter results[${filter?.size}], name results[${results.size}], intersection results[$size]")
            }
        }
        .map { it.toList() }
        .map { mapper.apply(it) }
        .asLiveData(viewModelScope.coroutineContext)

    fun searchByName(name: String) {
        searchUseCase.search(name)
    }

    fun clearOnGoingSearches() {
        searchUseCase.clearOnGoingSearch()
        filter.cancel()
    }

    fun updateFilter(drinkFilter: DrinkFilter) {
        filter.updateFilter(drinkFilter)
    }

    override fun onCleared() {
        super.onCleared()
        clearOnGoingSearches()
    }
}