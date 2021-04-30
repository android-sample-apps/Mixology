package com.yanivsos.mixological.v2.favorites.utils

import com.yanivsos.mixological.database.DrinkPreviewModel
import com.yanivsos.mixological.database.WatchlistItemModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@JvmName("mergeWithFavoritesDrinkPreviewModel")
fun Flow<List<DrinkPreviewModel>>.mergeWithFavorites(
    favoritesFlow: Flow<List<DrinkPreviewModel>>,
    defaultDispatcher: CoroutineDispatcher
): Flow<List<DrinkPreviewModel>> {
    val favoriteIds =
        favoritesFlow.map { favoritesList -> favoritesList.map { favorite -> FavoriteId(favorite.id) } }
    return mergeWithFavoritesIds(favoriteIds, defaultDispatcher)
}

fun Flow<List<DrinkPreviewModel>>.mergeWithFavorites(
    favoritesFlow: Flow<List<WatchlistItemModel>>,
    defaultDispatcher: CoroutineDispatcher
): Flow<List<DrinkPreviewModel>> {
    val favoriteIds =
        favoritesFlow.map { favoritesList -> favoritesList.map { favorite -> FavoriteId(favorite.id) } }
    return mergeWithFavoritesIds(favoriteIds, defaultDispatcher)
}

private fun Flow<List<DrinkPreviewModel>>.mergeWithFavoritesIds(
    favoritesFlow: Flow<List<FavoriteId>>,
    defaultDispatcher: CoroutineDispatcher
): Flow<List<DrinkPreviewModel>> {
    return combine(favoritesFlow) { previews, favorites ->
        withContext(defaultDispatcher) {
            previews.mergeWithFavorites(favorites)
        }
    }
}

@JvmName("mergeWithFavoritesDrinkPreviewModel")
fun List<DrinkPreviewModel>.mergeWithFavorites(
    favorites: List<DrinkPreviewModel>
): List<DrinkPreviewModel> {
    return mergeWithFavorites(favorites.map { FavoriteId(it.id) })
}

private fun List<DrinkPreviewModel>.mergeWithFavorites(
    favorites: List<FavoriteId>
): List<DrinkPreviewModel> {
    val favoritesSet = favorites.map { it.id }.toSet()
    return map { it.copy(isFavorite = it.id in favoritesSet) }
}

private data class FavoriteId(val id: String)
