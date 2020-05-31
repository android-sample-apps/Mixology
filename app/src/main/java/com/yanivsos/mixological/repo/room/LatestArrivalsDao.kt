package com.yanivsos.mixological.repo.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yanivsos.mixological.domain.models.LatestArrivalsModel
import kotlinx.coroutines.flow.Flow

@Dao
interface LatestArrivalsDao {

    @Query("SELECT * FROM LatestArrivalsModel")
    fun getAll(): Flow<List<LatestArrivalsModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(latestArrivals: List<LatestArrivalsModel>)

    @Query("DELETE FROM LatestArrivalsModel")
    fun removeAll()
}