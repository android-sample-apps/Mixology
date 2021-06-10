package com.yanivsos.mixological.v2.categories.useCases

import com.yanivsos.mixological.database.DrinkPreviewModel
import com.yanivsos.mixological.database.WatchlistItemModel
import com.yanivsos.mixological.v2.categories.repo.CategoriesRepository
import com.yanivsos.mixological.v2.drink.repo.DrinkRepository
import com.yanivsos.mixological.v2.favorites.utils.mergeWithFavorites
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
    private val drinksByCategoryStateFlow =
        MutableStateFlow<SelectedCategoryState>(SelectedCategoryState.None)

    val drinksByCategory: Flow<SelectedCategoryState> =
        drinksByCategoryStateFlow
            .combine(drinkRepository.getFavoritesWatchlist()) { categoryModel, favorites ->
                mergeWithFavorites(categoryModel, favorites)
            }

    private suspend fun mergeWithFavorites(
        categoryModel: SelectedCategoryState,
        favorites: List<WatchlistItemModel>
    ): SelectedCategoryState {
        return withContext(defaultDispatcher) {
            Timber.d("merging with favorites: ${favorites.map { it.id }}")
            when (categoryModel) {
                SelectedCategoryState.None -> SelectedCategoryState.None
                is SelectedCategoryState.Selected -> SelectedCategoryState.Selected(
                    SelectedCategoryModel(
                        name = categoryModel.model.name,
                        drinks = categoryModel.model.drinks.mergeWithFavorites(favorites)
                    )
                )
            }
        }
    }

    suspend fun updateCategory(categoryName: String) {
        runCatching {
            categoriesRepository.fetchByCategory(categoryName)
        }.onSuccess { drinks ->
            drinkRepository.storePreviews(drinks)
            drinksByCategoryStateFlow.value = SelectedCategoryState.Selected(
                SelectedCategoryModel(
                    name = categoryName,
                    drinks = drinks
                )
            )
        }.onFailure {
            Timber.e(it, "failed to update category $categoryName")
            drinksByCategoryStateFlow.value = SelectedCategoryState.Selected(
                SelectedCategoryModel(
                    name = categoryName
                )
            )
        }
    }
}

sealed class SelectedCategoryState {
    object None : SelectedCategoryState()
    data class Selected(val model: SelectedCategoryModel) : SelectedCategoryState()
}

data class SelectedCategoryModel(
    val name: String,
    val drinks: List<DrinkPreviewModel> = emptyList()
)
