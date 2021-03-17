package com.yanivsos.mixological.v2.drink

import androidx.lifecycle.ViewModel
import com.yanivsos.mixological.v2.drink.states.IngredientsState
import kotlinx.coroutines.flow.Flow

class DrinkViewModel: ViewModel() {

    val ingredientsState: Flow<IngredientsState> = TODO()
}
