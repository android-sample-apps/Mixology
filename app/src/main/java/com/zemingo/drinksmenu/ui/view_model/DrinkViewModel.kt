package com.zemingo.drinksmenu.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zemingo.drinksmenu.domain.*
import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.domain.models.WatchlistItemModel
import com.zemingo.drinksmenu.ui.models.DrinkUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.function.Function

class DrinkViewModel(
    private val getDrinkUseCase: GetDrinkUseCase,
    addToRecentlyViewedUseCase: AddToRecentlyViewedUseCase,
    getWatchlistUseCase: GetWatchlistUseCase,
    private val toggleWatchlistUseCase: ToggleWatchlistUseCase,
    mapper: Function<DrinkModel, DrinkUiModel>,
    private val drinkId: String
) : ViewModel() {

    init {
        addToRecentlyViewedUseCase.add(drinkId)
    }

    val isFavoriteLiveData: LiveData<Boolean> =
        getWatchlistUseCase
            .getById(drinkId)
            .map { it != null }
            .asLiveData()

    val drink: LiveData<DrinkUiModel> =
        getDrinkUseCase
            .drinkChannel
            .map { mapper.apply(it) }
            .asLiveData()

    fun toggleFavorite() {
        GlobalScope.launch(Dispatchers.IO) {
            toggleWatchlistUseCase.toggle(WatchlistItemModel(drinkId))
        }
    }

    override fun onCleared() {
        super.onCleared()
        getDrinkUseCase.cancel()
    }
}