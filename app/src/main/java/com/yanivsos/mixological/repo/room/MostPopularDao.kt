package com.yanivsos.mixological.repo.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yanivsos.mixological.domain.models.MostPopularModel
import kotlinx.coroutines.flow.Flow

@Dao
interface MostPopularDao {

    @Query("SELECT * FROM MostPopularModel")
    fun getAll(): Flow<List<MostPopularModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(latestArrivals: List<MostPopularModel>)

    @Query("DELETE FROM MostPopularModel WHERE drinkId = :ids")
    fun remove(ids: List<String>)

    @Query("DELETE FROM MostPopularModel")
    fun removeAll()
}