package com.yanivsos.mixological.v2.search.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yanivsos.mixological.database.AlcoholicFilterModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AlcoholicFilterDao {

    @Query("SELECT * FROM alcoholic_filters ORDER BY name")
    fun getAll(): Flow<List<AlcoholicFilterModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun store(alcoholicFilters: List<AlcoholicFilterModel>)
}
