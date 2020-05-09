package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkFilter
import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.repo.repositories.AdvancedSearchRepository

class FilterDrinkUseCase(
    private val advancedSearchRepository: AdvancedSearchRepository
) : SearchUseCase<DrinkFilter, DrinkPreviewModel>() {

    override suspend fun fetchQuery(filter: DrinkFilter): List<DrinkPreviewModel> {
        return advancedSearchRepository.filterBy(filter)
    }

}