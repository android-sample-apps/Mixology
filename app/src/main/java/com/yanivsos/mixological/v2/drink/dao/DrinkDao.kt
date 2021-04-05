package com.yanivsos.mixological.v2.drink.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yanivsos.mixological.database.DrinkModel
import com.yanivsos.mixological.database.DrinkPreviewModel
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinkDao {

    @Query("SELECT * FROM drinks WHERE id = :id")
    fun getById(id: String): Flow<DrinkModel?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun store(drinkModel: DrinkModel)

    @Query("SELECT * FROM drink_previews ORDER BY name")
    fun getPreviews(): Flow<List<DrinkPreviewModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun storePreviews(previews: List<DrinkPreviewModel>)
}
