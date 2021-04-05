package com.yanivsos.mixological.v2.search.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yanivsos.mixological.database.GlassModel
import kotlinx.coroutines.flow.Flow

@Dao
interface GlassDao {

    @Query("SELECT * FROM glasses ORDER BY name")
    fun getAll(): Flow<List<GlassModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun store(glasses: List<GlassModel>)
}
