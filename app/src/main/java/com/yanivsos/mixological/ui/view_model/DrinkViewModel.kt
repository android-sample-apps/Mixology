/*
package com.yanivsos.mixological.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.yanivsos.mixological.analytics.AnalyticsDispatcher
import com.yanivsos.mixological.analytics.ScreenNames
import com.yanivsos.mixological.domain.AddToRecentlyViewedUseCase
import com.yanivsos.mixological.domain.GetDrinkUseCase
import com.yanivsos.mixological.domain.ToggleWatchlistUseCase
import com.yanivsos.mixological.domain.models.DrinkModel
import com.yanivsos.mixological.domain.models.Result
import com.yanivsos.mixological.domain.models.WatchlistItemModel
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.models.DrinkUiModel
import com.yanivsos.mixological.ui.models.ResultUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.util.function.Function

class DrinkViewModel(
    private val getDrinkUseCase: GetDrinkUseCase,
    addToRecentlyViewedUseCase: AddToRecentlyViewedUseCase,
    private val toggleWatchlistUseCase: ToggleWatchlistUseCase,
    resultMapper: Function<Result<DrinkModel>, ResultUiModel<DrinkUiModel>>,
    private val drinkId: String
) : ViewModel() {

    init {
        Timber.d("init called")
        addToRecentlyViewedUseCase.add(drinkId)
    }

    val drinkFlow = getDrinkUseCase
        .drinkChannel
        .map { resultMapper.apply(it) }
        .distinctUntilChanged()

    val drink: LiveData<ResultUiModel<DrinkUiModel>> =
        drinkFlow
            .flowOn(Dispatchers.IO)
            .asLiveData()

    suspend fun toggleFavorite(drinkPreviewUiModel: DrinkPreviewUiModel): Boolean {
        return toggleWatchlistUseCase.toggle(WatchlistItemModel(drinkId))
            .also {
                reportFavoriteAnalytics(drinkPreviewUiModel, it)
            }

    }

    private fun reportFavoriteAnalytics(
        drinkPreviewUiModel: DrinkPreviewUiModel,
        isFavorite: Boolean
    ) {
        AnalyticsDispatcher
            .toggleFavorites(
                drinkPreviewUiModel,
                isFavorite,
                ScreenNames.DRINK
            )
    }

    fun refreshDrink() {
        Timber.i("refresh drink called for drinkId[$drinkId]")
        getDrinkUseCase.refresh()
    }

    override fun onCleared() {
        super.onCleared()
        getDrinkUseCase.cancel()
    }
}
*/
