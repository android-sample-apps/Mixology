package com.yanivsos.mixological.v2.drink.viewModel

import android.app.Application
import android.text.SpannableString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanivsos.mixological.analytics.AnalyticsDispatcher
import com.yanivsos.mixological.analytics.ScreenNames
import com.yanivsos.mixological.domain.models.WatchlistItemModel
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.models.DrinkUiModel
import com.yanivsos.mixological.ui.models.IngredientUiModel
import com.yanivsos.mixological.v2.drink.mappers.toUiModel
import com.yanivsos.mixological.v2.drink.useCases.GetOrFetchDrinkResult
import com.yanivsos.mixological.v2.drink.useCases.GetOrFetchDrinkUseCase
import com.yanivsos.mixological.v2.favorites.useCases.ToggleFavoriteUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

private const val INGREDIENTS_LOADING_ITEM_COUNT = 3
private const val METHOD_LOADING_ITEM_COUNT = 6

class DrinkViewModel(
    private val application: Application,
    private val getOrFetchDrinkUseCase: GetOrFetchDrinkUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    val drink: Flow<DrinkState> = getOrFetchDrinkUseCase
        .drinkFlow
        .map { toDrinkState(it) }

    val ingredients: Flow<IngredientsState> = drink
        .map { toIngredientState(it) }

    val method: Flow<MethodState> = drink
        .map { toMethodState(it) }

    init {
        // TODO: 17/03/2021 add to recently views db
        Timber.d("init: hashCode: ${hashCode()}")
    }

    override fun onCleared() {
        super.onCleared()
        getOrFetchDrinkUseCase.cancel("DrinkViewModel cleared")
    }

    // TODO: 18/03/2021 remove this to a shared flow callback
    fun toggleFavorite(drinkPreviewUiModel: DrinkPreviewUiModel) {
        viewModelScope.launch {
            toggleFavoriteUseCase
                .toggleFavorite(WatchlistItemModel(drinkPreviewUiModel.id))
                .also { reportFavoriteAnalytics(drinkPreviewUiModel, it) }
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

    private suspend fun toDrinkState(result: GetOrFetchDrinkResult): DrinkState {
        return withContext(defaultDispatcher) {
            when (result) {
                is GetOrFetchDrinkResult.Error -> DrinkState.Error(result.throwable, result.drinkId)
                GetOrFetchDrinkResult.Loading -> DrinkState.Loading
                is GetOrFetchDrinkResult.Success -> DrinkState.Success(
                    result.drinkModel.toUiModel(
                        application.applicationContext
                    )
                )
            }
        }
    }

    private suspend fun toIngredientState(drinkState: DrinkState): IngredientsState {
        return withContext(defaultDispatcher) {
            when (drinkState) {
                is DrinkState.Error -> IngredientsState.Error(drinkState.throwable)
                DrinkState.Loading -> IngredientsState.Loading(INGREDIENTS_LOADING_ITEM_COUNT)
                is DrinkState.Success -> IngredientsState.Success(drinkState.model.ingredients)
            }
        }
    }

    private suspend fun toMethodState(drinkState: DrinkState): MethodState {
        return withContext(defaultDispatcher) {
            when (drinkState) {
                is DrinkState.Error -> MethodState.Error(drinkState.throwable)
                DrinkState.Loading -> MethodState.Loading(METHOD_LOADING_ITEM_COUNT)
                is DrinkState.Success -> MethodState.Success(drinkState.model.instructions)
            }
        }
    }
}

sealed class DrinkState {
    object Loading : DrinkState()
    data class Success(val model: DrinkUiModel) : DrinkState()
    data class Error(val throwable: Throwable, val drinkId: String) : DrinkState()
}

sealed class IngredientsState {
    data class Loading(val itemCount: Int) : IngredientsState()
    data class Error(val throwable: Throwable) : IngredientsState()
    data class Success(val ingredients: List<IngredientUiModel>) : IngredientsState()
}

sealed class MethodState {
    data class Loading(val itemCount: Int) : MethodState()
    data class Success(val method: List<SpannableString>) : MethodState()
    data class Error(val throwable: Throwable) : MethodState()
}
