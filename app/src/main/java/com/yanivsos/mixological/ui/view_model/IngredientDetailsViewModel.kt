package com.yanivsos.mixological.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yanivsos.mixological.domain.GetIngredientDetailsUseCase
import com.yanivsos.mixological.domain.models.IngredientDetailsModel
import com.yanivsos.mixological.ui.models.IngredientDetailsUiModel
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