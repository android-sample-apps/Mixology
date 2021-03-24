package com.yanivsos.mixological.v2.search.repo

import com.yanivsos.mixological.repo.room.AlcoholicFilterDao
import com.yanivsos.mixological.repo.room.CategoryDao
import com.yanivsos.mixological.repo.room.GlassDao
import com.yanivsos.mixological.v2.ingredients.dao.IngredientDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepository(
    private val ingredientDao: IngredientDao,
    private val alcoholicFilterDao: AlcoholicFilterDao,
    private val categoryDao: CategoryDao,
    private val glassDao: GlassDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun getIngredients() = ingredientDao.getAll()

    fun getAlcoholicFilters() = alcoholicFilterDao.getAll()

    fun getCategories() = categoryDao.getAll()

    fun getGlasses() = glassDao.getAll()

    suspend fun findIngredientsBySimilarName(name: String) = withContext(ioDispatcher) {
        ingredientDao.findIngredientsBySimilarName(name)
    }
}
