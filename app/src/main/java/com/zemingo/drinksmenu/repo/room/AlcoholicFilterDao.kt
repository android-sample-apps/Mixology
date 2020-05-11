package com.zemingo.drinksmenu.repo.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zemingo.drinksmenu.domain.models.AlcoholicFilterModel
import com.zemingo.drinksmenu.domain.models.GlassModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AlcoholicFilterDao {

    @Query("SELECT * FROM AlcoholicFilterModel")
    fun getAll(): Flow<List<AlcoholicFilterModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun storeAll(alcoholicFilters: List<AlcoholicFilterModel>)
}