package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.repo.repositories.AdvancedSearchRepository

class SearchCocktailByNameUseCase(
    private val repository: AdvancedSearchRepository
) : SearchUseCase<DrinkModel>() {

    override suspend fun fetchQuery(query: String): List<DrinkModel> {
        return repository.fetchByName(query)
    }
}