package com.zemingo.drinksmenu.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zemingo.drinksmenu.domain.AddToRecentlyViewedUseCase
import com.zemingo.drinksmenu.domain.GetDrinkUseCase
import com.zemingo.drinksmenu.domain.GetWatchlistUseCase
import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.ui.models.DrinkUiModel
import kotlinx.coroutines.flow.map
import java.util.function.Function

class DrinkViewModel(
    getDrinkUseCase: GetDrinkUseCase,
    addToRecentlyViewedUseCase: AddToRecentlyViewedUseCase,
    getWatchlistUseCase: GetWatchlistUseCase,
    mapper: Function<DrinkModel, DrinkUiModel>,
    drinkId: String
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
            .getDrink(drinkId)
            .map { mapper.apply(it) }
            .asLiveData()

}