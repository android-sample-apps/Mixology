package com.yanivsos.mixological.v2.categories.useCases

import com.yanivsos.mixological.database.DrinkPreviewModel
import com.yanivsos.mixological.v2.categories.repo.CategoriesRepository
import com.yanivsos.mixological.v2.drink.repo.DrinkRepository
import com.yanivsos.mixological.v2.favorites.utils.mergeWithFavoritesPreviews
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import timber.log.Timber

class GetDrinksByCategoryUseCase(
    private val categoriesRepository: CategoriesRepository,
    private val drinkRepository: DrinkRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    private val drinksByCategoryStateFlow = MutableStateFlow<SelectedCategoryModel?>(null)

    val drinksByCategory: Flow<SelectedCategoryModel?> =
        drinksByCategoryStateFlow
            .combine(drinkRepository.getFavorites()) { categoryModel, favorites ->
                mergeWithFavorites(categoryModel, favorites)
            }

    private suspend fun mergeWithFavorites(
        categoryModel: SelectedCategoryModel?,
        favorites: List<DrinkPreviewModel>
    ): SelectedCategoryModel? {
        return withContext(defaultDispatcher) {
            Timber.d("merging with favorites: ${favorites.map { it.id }}")
            categoryModel?.let {
                it.copy(drinks = it.drinks.mergeWithFavoritesPreviews(favorites))
            }
        }
    }

    suspend fun updateCategory(categoryName: String) {
        runCatching {
            categoriesRepository.fetchByCategory(categoryName)
        }.onSuccess { drinks ->
            drinkRepository.storePreviews(drinks)
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
