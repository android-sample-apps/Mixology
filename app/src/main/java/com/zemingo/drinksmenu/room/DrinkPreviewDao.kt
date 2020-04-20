package com.zemingo.drinksmenu.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zemingo.drinksmenu.models.DrinkPreviewModel
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinkPreviewDao {

    @Query("SELECT * FROM DrinkPreviewModel")
    fun getAll(): Flow<List<DrinkPreviewModel>>

//    @Query("SELECT * FROM DrinkPreviewModel")
//    fun getByCategory(): Flow<List<DrinkPreviewModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(drinkPreviews: List<DrinkPreviewModel>)
}