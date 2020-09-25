package com.yanivsos.mixological.repo.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yanivsos.mixological.domain.models.IngredientModel
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {

    @Query("SELECT * FROM ingredients")
    fun getAll(): Flow<List<IngredientModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun storeAll(ingredients: List<IngredientModel>)

    @Query("SELECT * FROM ingredients WHERE name LIKE '%'||:name||'%'")
    fun getByName(name: String): Flow<List<IngredientModel>>
}