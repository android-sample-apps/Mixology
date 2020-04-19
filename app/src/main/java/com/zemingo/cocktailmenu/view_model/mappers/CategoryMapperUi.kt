package com.zemingo.cocktailmenu.view_model.mappers

import com.zemingo.cocktailmenu.models.CategoryEntity
import com.zemingo.cocktailmenu.models.CategoryUiModel
import java.util.function.Function

class CategoryMapperUi : Function<List<CategoryEntity>, List<CategoryUiModel>> {
    override fun apply(t: List<CategoryEntity>): List<CategoryUiModel> {
        return t.map {
            CategoryUiModel(
                name = it.name
            )
        }
    }
}