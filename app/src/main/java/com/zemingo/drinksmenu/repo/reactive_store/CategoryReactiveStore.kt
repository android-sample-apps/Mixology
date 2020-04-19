package com.zemingo.drinksmenu.repo.reactive_store

import com.zemingo.drinksmenu.models.CategoryEntity
import com.zemingo.drinksmenu.room.CategoryDao
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