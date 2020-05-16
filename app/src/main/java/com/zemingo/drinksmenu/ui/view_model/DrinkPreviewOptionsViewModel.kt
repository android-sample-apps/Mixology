package com.zemingo.drinksmenu.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zemingo.drinksmenu.domain.AddToWatchlistUseCase
import com.zemingo.drinksmenu.domain.GetWatchlistUseCase
import com.zemingo.drinksmenu.domain.models.WatchlistItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DrinkPreviewOptionsViewModel(
    private val addToWatchlistUseCase: AddToWatchlistUseCase,
    getWatchlistUseCase: GetWatchlistUseCase,
    id: String
) : ViewModel() {

    val inWatchlistLiveData: LiveData<Boolean> =
        getWatchlistUseCase
            .getById(id)
            .map { it != null }
            .asLiveData()

    fun addToWatchlist(id: String) {
        GlobalScope.launch(Dispatchers.IO) {
            addToWatchlistUseCase.addToWatchlist(
                WatchlistItemModel(id)
            )
        }
    }
}