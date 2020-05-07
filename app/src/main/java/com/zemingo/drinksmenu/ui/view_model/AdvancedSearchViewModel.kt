package com.zemingo.drinksmenu.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zemingo.drinksmenu.domain.SearchCocktailUseCase
import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import com.zemingo.drinksmenu.ui.models.DrinkUiModel
import kotlinx.coroutines.flow.map
import java.util.function.Function

class AdvancedSearchViewModel(
    private val searchCocktailUseCase: SearchCocktailUseCase,
    mapper: Function<List<DrinkModel>, List<DrinkUiModel>>,
    previewMapper: Function<List<DrinkUiModel>, List<DrinkPreviewUiModel>>
) : ViewModel() {

    val resultsLiveData: LiveData<List<DrinkPreviewUiModel>> = searchCocktailUseCase
        .searchChannel
        .map { mapper.apply(it) }
        .map { previewMapper.apply(it) }
        .asLiveData(viewModelScope.coroutineContext)

    fun searchByName(name: String) {
        searchCocktailUseCase.searchByName(name)
    }
}