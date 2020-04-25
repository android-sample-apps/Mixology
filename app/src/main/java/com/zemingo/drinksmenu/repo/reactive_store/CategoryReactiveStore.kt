package com.zemingo.drinksmenu.repo.reactive_store

import com.zemingo.drinksmenu.models.CategoryModel
import com.zemingo.drinksmenu.room.CategoryDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class CategoryReactiveStore(
    private val categoryDao: CategoryDao
) : ReactiveStore<String, CategoryModel, Void> {

    override fun storeAll(data: List<CategoryModel>) {
        categoryDao.insertAll(data)
    }

    override fun getAll(key: List<String>?): Flow<List<CategoryModel>> {
        return categoryDao.getAll()
    }

    override fun getByParam(param: Void): Flow<List<CategoryModel>> {
        return flowOf(emptyList())
    }

}