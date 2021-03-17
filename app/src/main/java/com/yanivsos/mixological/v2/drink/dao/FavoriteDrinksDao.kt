package com.yanivsos.mixological.v2.drink.dao

import androidx.room.Dao
import androidx.room.Query
import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.domain.models.WatchlistItemModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDrinksDao {

    @Query("SELECT * FROM watchlist")
    fun getAll(): Flow<List<DrinkPreviewModel>>

    @Query("SELECT * FROM drink_previews INNER JOIN watchlist ON drink_previews.id = watchlist.id ORDER BY name")
    fun getFavorites(): Flow<List<DrinkPreviewModel>>

    @Query("SELECT * FROM watchlist WHERE watchlist.id = id")
    fun getById(id: String): Flow<WatchlistItemModel?>
}
