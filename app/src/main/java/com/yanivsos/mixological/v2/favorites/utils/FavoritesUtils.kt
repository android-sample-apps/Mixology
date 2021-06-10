package com.yanivsos.mixological.v2.favorites.utils

import com.yanivsos.mixological.database.DrinkPreviewModel
import com.yanivsos.mixological.database.WatchlistItemModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

fun Flow<List<DrinkPreviewModel>>.mergeWithFavorites(
    favoritesFlow: Flow<List<WatchlistItemModel>>,
    defaultDispatcher: CoroutineDispatcher
): Flow<List<DrinkPreviewModel>> {
    return mergeWithFavoritesIds(
        favoritesFlow.asFavoriteIdsFlow(),
        defaultDispatcher
    )
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

private fun Flow<List<WatchlistItemModel>>.asFavoriteIdsFlow():
        Flow<List<FavoriteId>> =
    map { favoritesList -> favoritesList.toFavoriteIds() }

private fun List<WatchlistItemModel>.toFavoriteIds(): List<FavoriteId> =
    map { favorite -> FavoriteId(favorite.id) }

@JvmName("mergeWithFavoritesListDrinkPreviewModel")
fun List<DrinkPreviewModel>.mergeWithFavorites(
    favorites: List<WatchlistItemModel>,
): List<DrinkPreviewModel> {
    val favoriteIds = favorites.map { favorite -> FavoriteId(favorite.id) }
    return mergeWithFavorites(favoriteIds)
}

private fun List<DrinkPreviewModel>.mergeWithFavorites(
    favorites: List<FavoriteId>
): List<DrinkPreviewModel> {
    val favoritesSet = favorites.map { it.id }.toSet()
    return map { preview -> preview.copy(isFavorite = preview.id in favoritesSet) }
}

private data class FavoriteId(val id: String)
