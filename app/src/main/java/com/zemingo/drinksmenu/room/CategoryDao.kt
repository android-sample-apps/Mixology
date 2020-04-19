package com.zemingo.drinksmenu.room

import androidx.room.*
import com.zemingo.drinksmenu.models.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM CategoryEntity")
    fun getAll(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(categories: List<CategoryEntity>)
}