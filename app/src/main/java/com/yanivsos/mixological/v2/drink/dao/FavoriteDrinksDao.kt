package com.yanivsos.mixological.v2.drink.dao

import androidx.room.Dao
import androidx.room.Query
import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.domain.models.WatchlistItemModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDrinksDao {

    @Query("SELECT * FROM watchlist WHERE watchlist.id = :id")
    fun getById(id: String): Flow<WatchlistItemModel?>
}
