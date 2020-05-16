package com.zemingo.drinksmenu.ui.view_model

import androidx.lifecycle.ViewModel
import com.zemingo.drinksmenu.domain.AddToWatchlistUseCase
import com.zemingo.drinksmenu.domain.models.WatchlistItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DrinkPreviewOptionsViewModel(
    private val addToWatchlistUseCase: AddToWatchlistUseCase
) : ViewModel() {

    fun addToWatchlist(id: String) {
        GlobalScope.launch(Dispatchers.IO) {
            addToWatchlistUseCase.addToWatchlist(
                WatchlistItemModel(id)
            )
        }
    }
}