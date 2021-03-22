package com.yanivsos.mixological.v2.drinkOptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanivsos.mixological.domain.models.WatchlistItemModel
import com.yanivsos.mixological.v2.drink.useCases.DrinkModelResult
import com.yanivsos.mixological.v2.drink.useCases.GetDrinkUseCase
import com.yanivsos.mixological.v2.favorites.useCases.AddToFavoritesUseCase
import com.yanivsos.mixological.v2.favorites.useCases.RemoveFromFavoritesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DrinkPreviewOptionsViewModel(
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    getDrinkUseCase: GetDrinkUseCase,
    private val drinkId: String,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    val favorite =
        getDrinkUseCase
            .drink
            .map { mapToUiModel(it) }
            .stateIn(viewModelScope, SharingStarted.Lazily, FavoriteUiModel())


    private suspend fun mapToUiModel(result: DrinkModelResult): FavoriteUiModel {
        return withContext(defaultDispatcher) {
            when (result) {
                is DrinkModelResult.NotFount -> false
                is DrinkModelResult.Found -> result.drinkModel.isFavorite
            }.let { isFavorite ->
                FavoriteUiModel(isFavorite)
            }
        }
    }

    fun addToFavorites() {
        viewModelScope.launch {
            addToFavoritesUseCase.addToFavorite(WatchlistItemModel(drinkId))
        }
    }

    fun removeFromFavorites() {
        viewModelScope.launch {
            removeFromFavoritesUseCase.removeFromFavorites(WatchlistItemModel(drinkId))
        }
    }
}

data class FavoriteUiModel(val isFavorite: Boolean = false)
