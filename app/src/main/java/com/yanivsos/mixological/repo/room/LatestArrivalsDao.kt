package com.yanivsos.mixological.repo.room

import androidx.room.*
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

    @Transaction
    fun replaceAll(latestArrivals: List<LatestArrivalsModel>) {
        removeAll()
        insertAll(latestArrivals)
    }
}