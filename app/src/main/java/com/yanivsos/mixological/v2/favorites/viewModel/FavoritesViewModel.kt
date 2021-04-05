package com.yanivsos.mixological.v2.favorites.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanivsos.mixological.database.DrinkPreviewModel
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.v2.drink.mappers.toUiModel
import com.yanivsos.mixological.v2.favorites.useCases.GetFavoritesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

class FavoritesViewModel(
    getFavoritesUseCase: GetFavoritesUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    val favoritesState: Flow<FavoritesState> =
        getFavoritesUseCase
            .favorites
            .map { mapToState(it) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(),
                FavoritesState.Loading
            )

    private suspend fun mapToState(previews: List<DrinkPreviewModel>): FavoritesState.Success {
        return withContext(defaultDispatcher) {
            FavoritesState.Success(
                favorites = previews.map { it.toUiModel() })
        }
    }
}

sealed class FavoritesState {
    object Loading : FavoritesState()
    data class Success(val favorites: List<DrinkPreviewUiModel>) : FavoritesState()
}
