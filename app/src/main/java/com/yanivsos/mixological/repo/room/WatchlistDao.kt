package com.yanivsos.mixological.repo.room

import androidx.room.*
import com.yanivsos.mixological.domain.models.WatchlistItemModel
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {

    @Query("SELECT * FROM WatchlistItemModel")
    fun getAll(): Flow<List<WatchlistItemModel>>

    @Query("SELECT * FROM WatchlistItemModel WHERE id = :id")
    fun getById(id: String): Flow<List<WatchlistItemModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun store(watchlistItemModel: WatchlistItemModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun storeAll(watchList: List<WatchlistItemModel>)

    @Query("DELETE FROM WatchlistItemModel WHERE id = :id")
    fun remove(id: String)

    @Query("DELETE FROM WatchlistItemModel WHERE id IN (:ids)")
    fun remove(ids: List<String>)
}