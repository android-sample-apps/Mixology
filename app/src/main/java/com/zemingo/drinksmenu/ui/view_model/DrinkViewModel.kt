package com.zemingo.drinksmenu.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zemingo.drinksmenu.domain.*
import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.domain.models.Result
import com.zemingo.drinksmenu.domain.models.WatchlistItemModel
import com.zemingo.drinksmenu.ui.models.DrinkUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.function.Function

class DrinkViewModel(
    private val getDrinkUseCase: GetDrinkUseCase,
    private val fetchAndStoreDrinkUseCase: FetchAndStoreDrinkUseCase,
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

    val drink: LiveData<Result<DrinkUiModel>> =
        getDrinkUseCase
            .drinkChannel
            .map { result ->
                val drinkResult: Result<DrinkUiModel> = when (result) {
                    is Result.Success -> Result.Success(
                        mapper.apply(
                            result.data
                        )
                    )
                    is Result.Error -> Result.Error(
                        result.tr
                    )
                }
                drinkResult
            }
            .asLiveData()

    fun toggleFavorite() {
        GlobalScope.launch(Dispatchers.IO) {
            toggleWatchlistUseCase.toggle(WatchlistItemModel(drinkId))
        }
    }

    suspend fun refreshDrink(): Boolean {
        Timber.i("refresh drink called for drinkId[$drinkId]")
        return try {
            fetchAndStoreDrinkUseCase.fetchAndStore(drinkId)
            true
        } catch (e: Exception) {
            Timber.e(e, "refreshDrink failed for drinkId[$drinkId]")
            false
        }
    }

    override fun onCleared() {
        super.onCleared()
        getDrinkUseCase.cancel()
    }
}