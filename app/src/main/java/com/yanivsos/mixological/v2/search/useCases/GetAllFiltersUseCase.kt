package com.yanivsos.mixological.v2.search.useCases

import com.yanivsos.mixological.v2.ingredients.repository.IngredientDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllFiltersUseCase(
    ingredientDetailsRepository: IngredientDetailsRepository,
) {
    val allFilters: Flow<SelectedFilters> =
        ingredientDetailsRepository
            .getIngredients()
            .map { ingredients ->
                SelectedFilters(
                    ingredients = ingredients.map { FilterModel(it.name) }
                )
            }
}

data class SelectedFilters(
    val ingredients: List<FilterModel> = emptyList(),
    val alcoholic: List<FilterModel> = emptyList(),
    val glasses: List<FilterModel> = emptyList(),
    val categories: List<FilterModel> = emptyList()
)

data class FilterModel(
    val name: String,
    val isSelected: Boolean = false
)
