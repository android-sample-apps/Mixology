package com.yanivsos.mixological.repo.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yanivsos.mixological.domain.models.AlcoholicFilterModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AlcoholicFilterDao {

    @Query("SELECT * FROM alcoholic_filters")
    fun getAll(): Flow<List<AlcoholicFilterModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun storeAll(alcoholicFilters: List<AlcoholicFilterModel>)
}