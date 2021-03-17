package com.yanivsos.mixological.v2.drink.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanivsos.mixological.v2.drink.useCases.FetchAndStoreDrinkUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

class DrinkErrorViewModel(
    private val fetchAndStoreDrinkUseCase: FetchAndStoreDrinkUseCase
) : ViewModel() {

    fun refreshDrink(drinkId: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                fetchAndStoreDrinkUseCase
                    .fetchAndStore(drinkId)
            }.onFailure { Timber.e(it, "Failed to refresh drink") }
                .onSuccess { Timber.d("refreshed drink [$drinkId] successfully") }
        }
    }
}
