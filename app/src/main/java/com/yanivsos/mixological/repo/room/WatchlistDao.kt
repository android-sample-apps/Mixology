package com.yanivsos.mixological.repo.room

import androidx.room.*
import com.yanivsos.mixological.domain.models.WatchlistItemModel
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {

    @Query("SELECT * FROM watchlist")
    fun getAll(): Flow<List<WatchlistItemModel>>

    @Query("SELECT * FROM watchlist WHERE id = :id")
    fun getById(id: String): Flow<List<WatchlistItemModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun storeAll(watchList: List<WatchlistItemModel>)

    @Query("DELETE FROM watchlist WHERE id = :id")
    fun remove(id: String)

    @Query("DELETE FROM watchlist WHERE id IN (:ids)")
    fun remove(ids: List<String>)
}