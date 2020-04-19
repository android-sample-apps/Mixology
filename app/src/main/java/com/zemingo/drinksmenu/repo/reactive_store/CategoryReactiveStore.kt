package com.zemingo.drinksmenu.repo.reactive_store

import com.zemingo.drinksmenu.models.CategoryModel
import com.zemingo.drinksmenu.room.CategoryDao
import kotlinx.coroutines.flow.Flow

class CategoryReactiveStore(
    private val categoryDao: CategoryDao
) : ReactiveStore<CategoryModel> {

    override fun getAll(): Flow<List<CategoryModel>> {
        return categoryDao.getAll()
    }

    override fun storeAll(data: List<CategoryModel>) {
        categoryDao.insertAll(data)
    }
}