package com.yanivsos.mixological.v2.search.dao

import androidx.room.*
import com.yanivsos.mixological.domain.models.CategoryModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories ORDER BY name")
    fun getAll(): Flow<List<CategoryModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun store(categories: List<CategoryModel>)
}
