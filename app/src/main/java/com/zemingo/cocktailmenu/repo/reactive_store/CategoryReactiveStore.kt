package com.zemingo.cocktailmenu.repo.reactive_store

import com.zemingo.cocktailmenu.models.CategoryEntity
import com.zemingo.cocktailmenu.room.CategoryDao
import kotlinx.coroutines.flow.Flow

class CategoryReactiveStore(
    private val categoryDao: CategoryDao
) : ReactiveStore<CategoryEntity> {

    override fun getAll(): Flow<List<CategoryEntity>> {
        return categoryDao.getAll()
    }

    override fun storeAll(data: List<CategoryEntity>) {
        categoryDao.insertAll(data)
    }
}