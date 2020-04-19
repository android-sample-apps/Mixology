package com.zemingo.drinksmenu.repo.mappers

import com.zemingo.drinksmenu.models.CategoryEntity
import com.zemingo.drinksmenu.models.CategoryResponse
import com.zemingo.drinksmenu.models.DrinksWrapperResponse
import java.util.function.Function

class CategoryMapper : Function<DrinksWrapperResponse<CategoryResponse>, List<CategoryEntity>> {
    override fun apply(t: DrinksWrapperResponse<CategoryResponse>): List<CategoryEntity> {
        return t.data.map {
            CategoryEntity(
                name = it.category
            )
        }
    }
}