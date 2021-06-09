package com.yanivsos.mixological.v2.favorites.utils

import com.yanivsos.mixological.database.DrinkPreviewModel
import com.yanivsos.mixological.database.WatchlistItemModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Deprecated("remove this before version release")
@JvmName("mergeWithFavoritesDrinkPreviewModel")
fun Flow<List<DrinkPreviewModel>>.mergeWithFavorites(
    favoritesFlow: Flow<List<DrinkPreviewModel>>,
    defaultDispatcher: CoroutineDispatcher
): Flow<List<DrinkPreviewModel>> {
    val favoriteIds =
        favoritesFlow.map { favoritesList -> favoritesList.map { favorite -> FavoriteId(favorite.id) } }
    return mergeWithFavoritesIds(favoriteIds, defaultDispatcher)
}

//ok
fun Flow<List<DrinkPreviewModel>>.mergeWithFavorites(
    favoritesFlow: Flow<List<WatchlistItemModel>>,
    defaultDispatcher: CoroutineDispatcher
): Flow<List<DrinkPreviewModel>> {
    return mergeWithFavoritesIds(
        favoritesFlow.asFavoriteIdsFlow(),
        defaultDispatcher
    )
}


//ok
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

//ok
private fun Flow<List<WatchlistItemModel>>.asFavoriteIdsFlow():
        Flow<List<FavoriteId>> =
    map { favoritesList -> favoritesList.toFavoriteIds() }

//ok
private fun List<WatchlistItemModel>.toFavoriteIds(): List<FavoriteId> =
    map { favorite -> FavoriteId(favorite.id) }

@Deprecated("remove this before version release")
@JvmName("mergeWithFavoritesListDrinkPreviewModel")
fun List<DrinkPreviewModel>.mergeWithFavorites(
    favorites: List<WatchlistItemModel>,
): List<DrinkPreviewModel> {
    val favoriteIds = favorites.map { favorite -> FavoriteId(favorite.id) }
    return mergeWithFavorites(favoriteIds)
}

@Deprecated("remove this before version release")
@JvmName("mergeWithFavoritesDrinkPreviewModel")
fun List<DrinkPreviewModel>.mergeWithFavorites(
    favorites: List<DrinkPreviewModel>
): List<DrinkPreviewModel> {
    return mergeWithFavorites(favorites.map { FavoriteId(it.id) })
}

//ok
private fun List<DrinkPreviewModel>.mergeWithFavorites(
    favorites: List<FavoriteId>
): List<DrinkPreviewModel> {
    val favoritesSet = favorites.map { it.id }.toSet()
    return map { preview -> preview.copy(isFavorite = preview.id in favoritesSet) }
}

private data class FavoriteId(val id: String)
