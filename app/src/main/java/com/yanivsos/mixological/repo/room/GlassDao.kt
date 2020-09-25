package com.yanivsos.mixological.repo.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yanivsos.mixological.domain.models.GlassModel
import kotlinx.coroutines.flow.Flow

@Dao
interface GlassDao {

    @Query("SELECT * FROM glasses")
    fun getAll(): Flow<List<GlassModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun storeAll(glasses: List<GlassModel>)
}