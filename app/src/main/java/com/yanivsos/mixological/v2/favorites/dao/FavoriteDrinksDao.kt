package com.yanivsos.mixological.v2.favorites.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yanivsos.mixological.domain.models.WatchlistItemModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDrinksDao {

    @Query("SELECT * FROM watchlist WHERE watchlist.id = :id")
    fun getById(id: String): Flow<WatchlistItemModel?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun store(watchlistItemModel: WatchlistItemModel)

    @Query("DELETE FROM watchlist WHERE id = :id")
    suspend fun remove(id: String)
}
