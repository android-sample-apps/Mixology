package com.yanivsos.mixological.repo.mappers

import com.yanivsos.mixological.domain.models.CategoryModel
import com.yanivsos.mixological.repo.models.CategoryResponse
import com.yanivsos.mixological.repo.models.DrinksWrapperResponse
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