package com.zemingo.drinksmenu.repo.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.domain.models.PreviousSearchModel
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinkPreviewDao {

    @Query("SELECT * FROM DrinkPreviewModel")
    fun getAll(): Flow<List<DrinkPreviewModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(drinkPreviews: List<DrinkPreviewModel>)

    @Query("SELECT * FROM DrinkPreviewModel WHERE id IN (:ids)")
    fun getByIds(ids: List<String>): Flow<List<DrinkPreviewModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(searchModel: PreviousSearchModel)

    @Query("SELECT * FROM DrinkPreviewModel INNER JOIN PreviousSearchModel ON id = drinkId ORDER BY lastViewedTime DESC")
    fun getPreviousSearches(): Flow<List<DrinkPreviewModel>>
}