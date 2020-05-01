package com.zemingo.drinksmenu.repo.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.domain.models.PreviousSearchModel
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDrinkPreviewDao {

    @Query("SELECT * FROM PreviousSearchModel")
    fun getAll(): Flow<List<PreviousSearchModel>>

    @Query("SELECT * FROM PreviousSearchModel WHERE drinkId IN (:ids)")
    fun getAll(ids: List<String>): Flow<List<PreviousSearchModel>>

    @Query("SELECT DrinkPreviewModel.* FROM PreviousSearchModel INNER JOIN DrinkPreviewModel ON id = drinkId ORDER BY lastViewedTime DESC")
    fun getHistory(): Flow<List<DrinkPreviewModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(searches: List<PreviousSearchModel>)

}