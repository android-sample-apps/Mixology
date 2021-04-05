package com.yanivsos.mixological.v2.mappers

import com.yanivsos.mixological.database.CategoryModel
import com.yanivsos.mixological.ui.models.CategoryUiModel
import com.yanivsos.mixological.ui.models.SelectedCategoryUiModel
import com.yanivsos.mixological.v2.categories.useCases.CategoriesState
import com.yanivsos.mixological.v2.categories.useCases.SelectedCategoryModel
import com.yanivsos.mixological.v2.categories.viewModel.CategoriesUiState
import com.yanivsos.mixological.v2.drink.mappers.toUiModel

fun String.toLongId(): Long {
    return this.hashCode().toLong()
}

fun CategoriesState.toUiState(): CategoriesUiState {
    return when (this) {
        is CategoriesState.CategoryNotSelected ->
            CategoriesUiState.CategoryNotSelected(
                categories = categories.toUiModel()
            )
        is CategoriesState.CategorySelected ->
            CategoriesUiState.CategorySelected(
                categories = categories.toUiModel(),
                selectedCategoryUiModel = selectedCategory.toUiModel()
            )
    }
}

fun SelectedCategoryModel.toUiModel(): SelectedCategoryUiModel {
    return SelectedCategoryUiModel(
        name = name,
        drinks = drinks.toUiModel()
    )
}

fun CategoryModel.toUiModel(): CategoryUiModel {
    return CategoryUiModel(name)
}

fun List<CategoryModel>.toUiModel(): List<CategoryUiModel> {
    return map { it.toUiModel() }
}
