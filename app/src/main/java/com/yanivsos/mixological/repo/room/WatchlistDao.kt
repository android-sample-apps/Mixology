package com.yanivsos.mixological.repo.room

import androidx.room.*
import com.yanivsos.mixological.domain.models.WatchlistItemModel
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {

    //    @Query("SELECT * FROM DrinkPreviewModel INNER JOIN PreviousSearchModel ON id = drinkId ORDER BY lastViewedTime DESC")
//    @Query("SELECT * FROM DrinkPreviewModel INNER JOIN WatchlistItemModel ON id = id")
//    fun getAll(): Flow<List<DrinkPreviewModel>>

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
}