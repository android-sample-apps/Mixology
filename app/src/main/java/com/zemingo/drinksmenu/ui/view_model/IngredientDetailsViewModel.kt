package com.zemingo.drinksmenu.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zemingo.drinksmenu.domain.GetIngredientDetailsUseCase
import com.zemingo.drinksmenu.domain.models.IngredientDetailsModel
import com.zemingo.drinksmenu.ui.models.IngredientDetailsUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.function.Function

class IngredientDetailsViewModel(
    getIngredientDetailsUseCase: GetIngredientDetailsUseCase,
    mapper: Function<IngredientDetailsModel, IngredientDetailsUiModel>
) : ViewModel() {

    var job: Job? = null

    val ingredientDetailsLiveData: LiveData<IngredientDetailsUiModel> = getIngredientDetailsUseCase
        .ingredientDetails
        .map { mapper.apply(it) }
        .asLiveData()


    init {
        job = viewModelScope.launch(Dispatchers.IO) {
            getIngredientDetailsUseCase.launch()
        }
    }

    override fun onCleared() {
        Timber.d("onCleared called")
        super.onCleared()
        job?.cancel()
    }
}