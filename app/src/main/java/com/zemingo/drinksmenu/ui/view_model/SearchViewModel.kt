package com.zemingo.drinksmenu.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zemingo.drinksmenu.domain.GetDrinkPreviewUseCase
import com.zemingo.drinksmenu.domain.GetPreviousSearchResultsUseCase
import com.zemingo.drinksmenu.domain.MarkAsSearchedDrinkPreviewUseCase
import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.util.function.Function

class SearchViewModel(
    getDrinkPreviewUseCase: GetDrinkPreviewUseCase,
    getPreviousSearchResultsUseCase: GetPreviousSearchResultsUseCase,
    private val markAsSearchedDrinkPreviewUseCase: MarkAsSearchedDrinkPreviewUseCase,
    mapper: Function<List<DrinkPreviewModel>, List<DrinkPreviewUiModel>>
) : ViewModel() {

    val suggestions: LiveData<List<DrinkPreviewUiModel>> =
        getDrinkPreviewUseCase
            .getAll()
            .onEach { previews ->
                Timber.d("received suggestions: ${previews.map { it.name }}")
            }
            .map { mapper.apply(it) }
            .asLiveData()

    val previousSearches: LiveData<List<DrinkPreviewUiModel>> =
        getPreviousSearchResultsUseCase
            .previousSearches
            .map {
                mapper.apply(it)
            }
            .asLiveData()

    fun markAsSearched(drink: DrinkPreviewUiModel) {
        markAsSearchedDrinkPreviewUseCase.markAsSearched(drink.id)
    }
}