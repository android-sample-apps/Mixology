package com.yanivsos.mixological.v2.favorites.useCases

import com.yanivsos.mixological.domain.models.WatchlistItemModel
import com.yanivsos.mixological.v2.drink.repo.DrinkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import timber.log.Timber

class ToggleFavoriteUseCase(
    private val drinkRepository: DrinkRepository,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun toggleFavorite(watchlistItemModel: WatchlistItemModel): Boolean {
        Timber.d("toggleFavorite: $watchlistItemModel")
        return when (isFavorite(watchlistItemModel)) {
            false -> addToFavorites(watchlistItemModel)
            true -> removeFromFavorites(watchlistItemModel)
        }
    }

    private suspend fun isFavorite(watchlistItemModel: WatchlistItemModel): Boolean {
        return withContext(ioDispatcher) {
            drinkRepository
                .getFavoriteById(watchlistItemModel)
                .firstOrNull()?.let { true } ?: false
        }
    }

    private suspend fun addToFavorites(watchlistItemModel: WatchlistItemModel) =
        addToFavoritesUseCase.addToFavorite(watchlistItemModel).let { true }

    private suspend fun removeFromFavorites(watchlistItemModel: WatchlistItemModel) =
        removeFromFavoritesUseCase.removeFromFavorites(watchlistItemModel).let { false }
}
