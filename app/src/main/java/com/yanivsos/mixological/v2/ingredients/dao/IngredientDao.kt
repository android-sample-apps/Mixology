package com.yanivsos.mixological.v2.ingredients.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yanivsos.mixological.domain.models.IngredientDetailsModel
import com.yanivsos.mixological.domain.models.IngredientModel
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {

    @Query("SELECT * FROM ingredient_details WHERE name LIKE :name")
    fun getByName(name: String): Flow<List<IngredientDetailsModel>>

    @Query("SELECT * FROM ingredients ORDER BY name")
    fun getAll(): Flow<List<IngredientModel>>

    @Query("SELECT * FROM ingredients WHERE name LIKE '%'||:name||'%'")
    suspend fun findIngredientsBySimilarName(name: String): List<IngredientModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun store(ingredientDetails: IngredientDetailsModel)

}
