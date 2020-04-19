package com.zemingo.drinksmenu.view_model.mappers

import com.zemingo.drinksmenu.models.CategoryModel
import com.zemingo.drinksmenu.models.CategoryUiModel
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