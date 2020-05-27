package com.yanivsos.mixological.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.yanivsos.mixological.domain.GetWatchlistUseCase
import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
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