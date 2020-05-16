package com.zemingo.drinksmenu.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zemingo.drinksmenu.domain.GetWatchlistUseCase
import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import kotlinx.coroutines.flow.map
import java.util.function.Function

class WatchlistViewModel(
    getWatchlistUseCase: GetWatchlistUseCase,
    mapper: Function<List<DrinkPreviewModel>, List<DrinkPreviewUiModel>>
) : ViewModel() {

    val watchlist =
        getWatchlistUseCase
            .watchList
            .map { mapper.apply(it) }
            .asLiveData()
}