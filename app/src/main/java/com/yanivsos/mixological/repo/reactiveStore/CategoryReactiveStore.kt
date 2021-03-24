package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.CategoryModel
import com.yanivsos.mixological.repo.room.CategoryDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CategoryReactiveStore(
    private val categoryDao: CategoryDao
) : NonRemovableReactiveStore<CategoryModel, Unit> {

    override fun storeAll(data: List<CategoryModel>) {
        GlobalScope.launch { categoryDao.store(data) }

    }

    override fun get(param: Unit): Flow<List<CategoryModel>> {
        return categoryDao.getAll()
    }
}
