package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.repo.repositories.AdvancedSearchRepository

class SearchCocktailByNameUseCase(
    private val repository: AdvancedSearchRepository
) : SearchUseCase<String, DrinkModel>() {

    override suspend fun fetchQuery(filter: String): List<DrinkModel> {
        return repository.fetchByName(filter)
    }
}