package com.yanivsos.mixological.v2.search.useCases

import com.yanivsos.mixological.database.AlcoholicFilterModel
import com.yanivsos.mixological.database.CategoryModel
import com.yanivsos.mixological.database.GlassModel
import com.yanivsos.mixological.database.IngredientModel
import com.yanivsos.mixological.v2.search.repo.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetAllFiltersUseCase(
    searchRepository: SearchRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    val allFilters: Flow<SelectedFilters> =
        searchRepository
            .getIngredients()
            .map { ingredients -> ingredients.ingredientsToSelectedFilters() }
            .combine(searchRepository.getAlcoholicFilters()) { selectedFilter, alcoholics ->
                mergeAlcoholic(selectedFilter, alcoholics)
            }.combine(searchRepository.getCategories()) { selectedFilter, categories ->
                mergeCategories(selectedFilter, categories)
            }.combine(searchRepository.getGlasses()) { selectedFilter, glasses ->
                mergeGlasses(selectedFilter, glasses)
            }

    private suspend fun mergeAlcoholic(
        selectedFilter: SelectedFilters,
        alcoholics: List<AlcoholicFilterModel>
    ) = withContext(defaultDispatcher) {
        selectedFilter.copy(alcoholic = FilterCollection(alcoholics.map {
            FilterModel(it.name)
        }))
    }

    private suspend fun mergeCategories(
        selectedFilter: SelectedFilters,
        categories: List<CategoryModel>
    ) = withContext(defaultDispatcher) {
        selectedFilter.copy(categories = FilterCollection(categories.map {
            FilterModel(it.name)
        }))
    }

    private suspend fun mergeGlasses(
        selectedFilter: SelectedFilters,
        glasses: List<GlassModel>
    ) = withContext(defaultDispatcher) {
        selectedFilter.copy(glasses = FilterCollection(glasses.map {
            FilterModel(it.name)
        }))
    }

    private suspend fun List<IngredientModel>.ingredientsToSelectedFilters() =
        withContext(defaultDispatcher) {
            SelectedFilters(
                ingredients = FilterCollection(map { FilterModel(it.name) })
            )
        }
}

data class SelectedFilters(
    val ingredients: FilterCollection = FilterCollection(),
    val alcoholic: FilterCollection = FilterCollection(),
    val glasses: FilterCollection = FilterCollection(),
    val categories: FilterCollection = FilterCollection(),
    val ingredientKeyword: String? = null
)

data class FilterModel(
    val name: String,
    val isSelected: Boolean = false
)

data class FilterCollection(
    val filters: List<FilterModel> = emptyList(),
    val selectedCount: Int = 0
)

data class FilterOperators(
    val ingredientOperator: AccumulativeOperator = AccumulativeOperator.Union,
    val alcoholicOperator: AccumulativeOperator = AccumulativeOperator.Union,
    val glassesOperator: AccumulativeOperator = AccumulativeOperator.Union,
    val categoriesOperator: AccumulativeOperator = AccumulativeOperator.Union,
)

