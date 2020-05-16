package com.zemingo.drinksmenu.repo.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.domain.models.WatchlistItemModel
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {

    //    @Query("SELECT * FROM DrinkPreviewModel INNER JOIN PreviousSearchModel ON id = drinkId ORDER BY lastViewedTime DESC")
//    @Query("SELECT * FROM DrinkPreviewModel INNER JOIN WatchlistItemModel ON id = id")
//    fun getAll(): Flow<List<DrinkPreviewModel>>

    @Query("SELECT * FROM WatchlistItemModel")
    fun getAll(): Flow<List<WatchlistItemModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun store(watchlistItemModel: WatchlistItemModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun storeAll(watchList: List<WatchlistItemModel>)
}