package com.yanivsos.mixological.v2.categories.useCases

import com.yanivsos.mixological.database.CategoryModel
import com.yanivsos.mixological.ui.models.CategoryUiModel
import com.yanivsos.mixological.v2.categories.repo.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import timber.log.Timber

class GetCategoriesStateUseCase(
    categoriesRepository: CategoriesRepository,
    private val getDrinksByCategoryUseCase: GetDrinksByCategoryUseCase
) {
    val categories: Flow<CategoriesState> = categoriesRepository
        .getCategories()
        .combine(getDrinksByCategoryUseCase.drinksByCategory) { categories, drinksByCategory ->
            drinksByCategory?.let { CategoriesState.CategorySelected(
                categories = categories,
                selectedCategory = drinksByCategory
            ) } ?:
            CategoriesState.CategoryNotSelected(
                categories = categories
            )
        }

    suspend fun updateSelected(categoryUiModel: CategoryUiModel) {
        Timber.d("updating selected: $categoryUiModel")
        getDrinksByCategoryUseCase.updateCategory(categoryUiModel.name)
    }
}

sealed class CategoriesState {

    data class CategoryNotSelected(
        val categories: List<CategoryModel>
    ) : CategoriesState()

    data class CategorySelected(
        val categories: List<CategoryModel>,
        val selectedCategory: SelectedCategoryModel
    ) : CategoriesState()
}
