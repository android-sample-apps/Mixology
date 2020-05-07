package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.repo.repositories.AdvancedSearchRepository

class SearchCocktailByIngredientUseCase(
    private val repository: AdvancedSearchRepository
): SearchUseCase<DrinkPreviewModel>() {

    override suspend fun fetchQuery(query: String): List<DrinkPreviewModel> {
        return repository.fetchByIngredient(query)
    }
}