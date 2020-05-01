package com.zemingo.drinksmenu.repo.mappers

import com.zemingo.drinksmenu.domain.models.CategoryModel
import com.zemingo.drinksmenu.repo.models.CategoryResponse
import com.zemingo.drinksmenu.repo.models.DrinksWrapperResponse
import java.util.function.Function

class CategoryMapper : Function<DrinksWrapperResponse<CategoryResponse>, List<CategoryModel>> {
    override fun apply(t: DrinksWrapperResponse<CategoryResponse>): List<CategoryModel> {
        return t.data.map {
            CategoryModel(
                name = it.category
            )
        }
    }
}