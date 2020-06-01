package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.CategoryModel
import com.yanivsos.mixological.repo.room.CategoryDao
import kotlinx.coroutines.flow.Flow

class CategoryReactiveStore(
    private val categoryDao: CategoryDao
) : NonRemovableReactiveStore<CategoryModel, Unit> {

    override fun storeAll(data: List<CategoryModel>) {
        categoryDao.insertAll(data)
    }

    override fun get(param: Unit): Flow<List<CategoryModel>> {
        return categoryDao.getAll()
    }
}