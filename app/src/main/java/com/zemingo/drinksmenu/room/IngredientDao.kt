package com.zemingo.drinksmenu.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zemingo.drinksmenu.models.IngredientModel
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {

    @Query("SELECT * FROM IngredientModel")
    fun getAll(): Flow<IngredientModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun storeAll(ingredients: List<IngredientModel>)
}