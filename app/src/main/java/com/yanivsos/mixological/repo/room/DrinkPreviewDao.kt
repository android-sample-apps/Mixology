package com.yanivsos.mixological.repo.room

import androidx.room.*
import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.domain.models.RecentlyViewedModel
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinkPreviewDao {

    @Query("SELECT * FROM drink_previews")
    fun getAll(): Flow<List<DrinkPreviewModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(drinkPreviews: List<DrinkPreviewModel>)

    @Query("SELECT * FROM drink_previews WHERE id IN (:ids)")
    fun getByIds(ids: List<String>): Flow<List<DrinkPreviewModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(searchModel: RecentlyViewedModel)

    @Query("SELECT * FROM drink_previews INNER JOIN recently_viewed ON id = drinkId ORDER BY lastViewedTime DESC")
    fun getPreviousSearches(): Flow<List<DrinkPreviewModel>>
}