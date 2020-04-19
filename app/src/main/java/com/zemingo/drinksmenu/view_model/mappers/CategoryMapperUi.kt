package com.zemingo.drinksmenu.view_model.mappers

import com.zemingo.drinksmenu.models.CategoryEntity
import com.zemingo.drinksmenu.models.CategoryUiModel
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