package com.yanivsos.mixological.v2.drink.viewModel

import android.content.Context
import android.text.SpannableString
import androidx.lifecycle.ViewModel
import com.yanivsos.mixological.analytics.AnalyticsDispatcher
import com.yanivsos.mixological.analytics.ScreenNames
import com.yanivsos.mixological.domain.ToggleWatchlistUseCase
import com.yanivsos.mixological.domain.models.WatchlistItemModel
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.models.DrinkUiModel
import com.yanivsos.mixological.ui.models.IngredientUiModel
import com.yanivsos.mixological.v2.drink.mappers.toUiModel
import com.yanivsos.mixological.v2.drink.useCases.GetOrFetchDrinkResult
import com.yanivsos.mixological.v2.drink.useCases.GetOrFetchDrinkUseCase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

private const val INGREDIENTS_LOADING_ITEM_COUNT = 3
private const val METHOD_LOADING_ITEM_COUNT = 6

class DrinkViewModel(
    context: Context,
    private val getOrFetchDrinkUseCase: GetOrFetchDrinkUseCase,
    private val toggleWatchlistUseCase: ToggleWatchlistUseCase
) : ViewModel() {

    //TODO - fix this context warning
    private val appContext = context.applicationContext

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

    suspend fun toggleFavorite(drinkPreviewUiModel: DrinkPreviewUiModel): Boolean {
        return toggleWatchlistUseCase.toggle(WatchlistItemModel(drinkPreviewUiModel.id))
            .also { reportFavoriteAnalytics(drinkPreviewUiModel, it) }
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

    private fun toDrinkState(result: GetOrFetchDrinkResult): DrinkState {
        return when (result) {
            is GetOrFetchDrinkResult.Error -> DrinkState.Error(result.throwable, result.drinkId)
            GetOrFetchDrinkResult.Loading -> DrinkState.Loading
            is GetOrFetchDrinkResult.Success -> DrinkState.Success(
                result.drinkModel.toUiModel(
                    appContext
                )
            )
        }
    }

    private fun toIngredientState(drinkState: DrinkState): IngredientsState {
        return when (drinkState) {
            is DrinkState.Error -> IngredientsState.Error(drinkState.throwable)
            DrinkState.Loading -> IngredientsState.Loading(INGREDIENTS_LOADING_ITEM_COUNT)
            is DrinkState.Success -> IngredientsState.Success(drinkState.model.ingredients)
        }
    }

    private fun toMethodState(drinkState: DrinkState): MethodState {
        return when (drinkState) {
            is DrinkState.Error -> MethodState.Error(drinkState.throwable)
            DrinkState.Loading -> MethodState.Loading(METHOD_LOADING_ITEM_COUNT)
            is DrinkState.Success -> MethodState.Success(drinkState.model.instructions)
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
