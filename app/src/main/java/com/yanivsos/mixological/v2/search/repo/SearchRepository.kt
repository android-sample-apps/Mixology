package com.yanivsos.mixological.v2.search.repo

import com.yanivsos.mixological.repo.room.AlcoholicFilterDao
import com.yanivsos.mixological.repo.room.CategoryDao
import com.yanivsos.mixological.repo.room.GlassDao
import com.yanivsos.mixological.v2.ingredients.dao.IngredientDao

class SearchRepository(
    private val ingredientDao: IngredientDao,
    private val alcoholicFilterDao: AlcoholicFilterDao,
    private val categoryDao: CategoryDao,
    private val glassDao: GlassDao,
) {

    fun getIngredients() = ingredientDao.getAll()

    fun getAlcoholicFilters() = alcoholicFilterDao.getAll()

    fun getCategories() = categoryDao.getAll()

    fun getGlasses() = glassDao.getAll()
}
