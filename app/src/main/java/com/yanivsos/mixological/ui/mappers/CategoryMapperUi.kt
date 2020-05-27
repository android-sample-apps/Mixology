package com.yanivsos.mixological.ui.mappers

import com.yanivsos.mixological.domain.models.CategoryModel
import com.yanivsos.mixological.ui.models.CategoryUiModel
import java.util.function.Function

class CategoryMapperUi : Function<List<CategoryModel>, List<CategoryUiModel>> {
    override fun apply(t: List<CategoryModel>): List<CategoryUiModel> {
        return t.map {
            CategoryUiModel(
                name = it.name
            )
        }
    }
}