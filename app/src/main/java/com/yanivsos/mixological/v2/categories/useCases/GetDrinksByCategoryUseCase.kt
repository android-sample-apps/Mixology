package com.yanivsos.mixological.v2.categories.useCases

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.v2.categories.repo.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

class GetDrinksByCategoryUseCase(
    private val categoriesRepository: CategoriesRepository
) {
    private val drinksByCategoryStateFlow = MutableStateFlow<SelectedCategoryModel?>(null)
    val drinksByCategory: Flow<SelectedCategoryModel?> = drinksByCategoryStateFlow

    suspend fun updateCategory(categoryName: String) {
        runCatching {
            categoriesRepository.fetchByCategory(categoryName)
        }.onSuccess { drinks ->
            drinksByCategoryStateFlow.value = SelectedCategoryModel(
                name = categoryName,
                drinks = drinks
            )
        }.onFailure {
            Timber.e(it, "failed to update category $categoryName")
            drinksByCategoryStateFlow.value = SelectedCategoryModel(
                name = categoryName
            )
        }
    }
}

data class SelectedCategoryModel(
    val name: String,
    val drinks: List<DrinkPreviewModel> = emptyList()
)
