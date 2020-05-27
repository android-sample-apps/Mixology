package com.yanivsos.mixological.repo.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.domain.models.RecentlyViewedModel
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentlyViewedDao {

    @Query("SELECT * FROM RecentlyViewedModel")
    fun getAll(): Flow<List<RecentlyViewedModel>>

    @Query("SELECT * FROM RecentlyViewedModel WHERE drinkId IN (:ids)")
    fun getAll(ids: List<String>): Flow<List<RecentlyViewedModel>>

    @Query("SELECT DrinkPreviewModel.* FROM RecentlyViewedModel INNER JOIN DrinkPreviewModel ON id = drinkId ORDER BY lastViewedTime DESC")
    fun getRecentlyViewed(): Flow<List<DrinkPreviewModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(searches: List<RecentlyViewedModel>)

}