package com.zemingo.drinksmenu.repo.room

import androidx.room.*
import com.zemingo.drinksmenu.domain.models.CategoryModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM CategoryModel")
    fun getAll(): Flow<List<CategoryModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(categories: List<CategoryModel>)
}