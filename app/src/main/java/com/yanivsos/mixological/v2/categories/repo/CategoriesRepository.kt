package com.yanivsos.mixological.v2.categories.repo

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.repo.DrinkService
import com.yanivsos.mixological.v2.categories.dao.CategoriesDao
import com.yanivsos.mixological.v2.drink.mappers.toModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoriesRepository(
    private val categoriesDao: CategoriesDao,
    private val drinkService: DrinkService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend fun fetchByCategory(categoryName: String): List<DrinkPreviewModel> {
        return withContext(ioDispatcher) {
            val response = drinkService.filterByCategory(categoryName)
            withContext(defaultDispatcher) {
                response.toModel()
            }
        }
    }

    fun getCategories() = categoriesDao.getCategories()
}
