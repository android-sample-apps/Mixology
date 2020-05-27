package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.CategoryModel
import com.yanivsos.mixological.repo.room.CategoryDao
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

    override fun remove(key: String) {
        TODO("Not yet implemented")
    }

}