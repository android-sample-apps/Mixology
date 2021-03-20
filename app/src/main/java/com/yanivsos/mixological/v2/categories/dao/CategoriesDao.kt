package com.yanivsos.mixological.v2.categories.dao

import androidx.room.Dao
import androidx.room.Query
import com.yanivsos.mixological.domain.models.CategoryModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriesDao {

    @Query("SELECT * FROM categories")
    fun getCategories(): Flow<List<CategoryModel>>
}
