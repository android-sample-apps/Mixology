package com.zemingo.drinksmenu.repo.repositories

import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.repo.DrinkService
import com.zemingo.drinksmenu.repo.models.DrinkResponse
import com.zemingo.drinksmenu.repo.models.DrinksWrapperResponse
import java.util.function.Function

class AdvancedSearchRepository(
    private val service: DrinkService,
    private val mapper: Function<DrinksWrapperResponse<DrinkResponse>, List<DrinkModel>>
) {

    suspend fun fetchByName(name: String): List<DrinkModel> {
        val response = service.searchByName(name)
        return mapper.apply(response)
    }
}