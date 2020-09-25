package com.yanivsos.mixological.repo.room

import androidx.room.*
import com.yanivsos.mixological.domain.models.MostPopularModel
import kotlinx.coroutines.flow.Flow

@Dao
interface MostPopularDao {

    @Query("SELECT * FROM most_popular")
    fun getAll(): Flow<List<MostPopularModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(mostPopulars: List<MostPopularModel>)

    @Query("DELETE FROM most_popular")
    fun removeAll()

    @Transaction
    fun replaceAll(mostPopulars: List<MostPopularModel>) {
        removeAll()
        insertAll(mostPopulars)
    }
}