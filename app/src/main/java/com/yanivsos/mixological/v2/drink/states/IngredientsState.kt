package com.yanivsos.mixological.v2.drink.states

import com.yanivsos.mixological.ui.models.IngredientUiModel

sealed class IngredientsState {
    data class Loading(val itemCount: Int) : IngredientsState()
    data class Error(val throwable: Throwable) : IngredientsState()
    data class Success(val ingredients: List<IngredientUiModel>) : IngredientsState()
}
