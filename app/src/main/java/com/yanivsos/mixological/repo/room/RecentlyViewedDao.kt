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

    @Query("SELECT * FROM recently_viewed")
    fun getAll(): Flow<List<RecentlyViewedModel>>

    @Query("SELECT * FROM recently_viewed WHERE drinkId IN (:ids)")
    fun getAll(ids: List<String>): Flow<List<RecentlyViewedModel>>

    @Query("SELECT drink_previews.* FROM recently_viewed INNER JOIN drink_previews ON id = drinkId ORDER BY lastViewedTime DESC")
    fun getRecentlyViewed(): Flow<List<DrinkPreviewModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(searches: List<RecentlyViewedModel>)

}