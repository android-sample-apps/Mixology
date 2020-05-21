package com.zemingo.drinksmenu.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zemingo.drinksmenu.domain.*
import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.domain.models.Result
import com.zemingo.drinksmenu.domain.models.WatchlistItemModel
import com.zemingo.drinksmenu.ui.models.DrinkUiModel
import com.zemingo.drinksmenu.ui.models.ResultUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.function.Function

class DrinkViewModel(
    private val getDrinkUseCase: GetDrinkUseCase,
    addToRecentlyViewedUseCase: AddToRecentlyViewedUseCase,
    getWatchlistUseCase: GetWatchlistUseCase,
    private val toggleWatchlistUseCase: ToggleWatchlistUseCase,
    resultMapper: Function<Result<DrinkModel>, ResultUiModel<DrinkUiModel>>,
    private val drinkId: String
) : ViewModel() {

    init {
        Timber.d("init called")
        addToRecentlyViewedUseCase.add(drinkId)
    }

    val isFavoriteLiveData: LiveData<Boolean> =
        getWatchlistUseCase
            .getById(drinkId)
            .map { it != null }
            .asLiveData()

    val drinkFlow = getDrinkUseCase
        .drinkChannel
        .map { resultMapper.apply(it) }
        .distinctUntilChanged()

    val drink: LiveData<ResultUiModel<DrinkUiModel>> =
        drinkFlow
            .asLiveData()

    fun toggleFavorite() {
        GlobalScope.launch(Dispatchers.IO) {
            toggleWatchlistUseCase.toggle(WatchlistItemModel(drinkId))
        }
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