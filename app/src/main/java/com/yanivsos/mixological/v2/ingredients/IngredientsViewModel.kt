package com.yanivsos.mixological.v2.ingredients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanivsos.mixological.domain.models.IngredientDetailsModel
import com.yanivsos.mixological.ui.models.IngredientDetailsUiModel
import com.yanivsos.mixological.v2.ingredients.mappers.toUiModel
import com.yanivsos.mixological.v2.ingredients.useCases.GetIngredientDetailsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

class IngredientDetailsViewModel(
    getIngredientUseCase: GetIngredientDetailsUseCase,
    ingredientName: String,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    val ingredientDetails: Flow<IngredientDetailsState> =
        getIngredientUseCase
            .getByName(ingredientName)
            .map { mapToUiModel(it) }
            .stateIn(viewModelScope, SharingStarted.Lazily, IngredientDetailsState.Loading)

    private suspend fun mapToUiModel(detailsModel: IngredientDetailsModel?): IngredientDetailsState {
        return withContext(defaultDispatcher) {
            detailsModel?.let { IngredientDetailsState.Success(it.toUiModel()) }
                ?: IngredientDetailsState.NotFound
        }
    }
}

sealed class IngredientDetailsState {
    object Loading : IngredientDetailsState()
    object NotFound : IngredientDetailsState()
    data class Success(val model: IngredientDetailsUiModel) : IngredientDetailsState()
}
