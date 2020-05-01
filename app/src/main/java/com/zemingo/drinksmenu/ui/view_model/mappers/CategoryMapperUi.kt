package com.zemingo.drinksmenu.ui.view_model.mappers

import com.zemingo.drinksmenu.domain.models.CategoryModel
import com.zemingo.drinksmenu.ui.models.CategoryUiModel
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