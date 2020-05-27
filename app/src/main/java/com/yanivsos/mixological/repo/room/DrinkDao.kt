package com.yanivsos.mixological.repo.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yanivsos.mixological.domain.models.DrinkModel
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinkDao {

    @Query("SELECT * FROM DrinkModel")
    fun getAll(): Flow<List<DrinkModel>>

    @Query("SELECT * FROM DrinkModel WHERE id = :id")
    fun getById(id: String): Flow<List<DrinkModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun storeAll(drinks: List<DrinkModel>)

    @Query("DELETE FROM DrinkModel WHERE id = :id")
    fun remove(id: String)
}