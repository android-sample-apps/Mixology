package com.zemingo.drinksmenu.repo.repositories

import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.repo.DrinkService
import com.zemingo.drinksmenu.repo.models.DrinkPreviewResponse
import com.zemingo.drinksmenu.repo.models.DrinkResponse
import com.zemingo.drinksmenu.repo.models.DrinksWrapperResponse
import com.zemingo.drinksmenu.repo.models.NullableDrinksWrapperResponse
import java.util.function.Function

class AdvancedSearchRepository(
    private val service: DrinkService,
    private val drinkMapper: Function<NullableDrinksWrapperResponse<DrinkResponse>, List<DrinkModel>>,
    private val previewMapper: Function<DrinksWrapperResponse<DrinkPreviewResponse>, List<DrinkPreviewModel>>
) {

    suspend fun fetchByName(name: String): List<DrinkModel> {
        val response = service.searchByName(name)
        return drinkMapper.apply(response)
    }

    suspend fun fetchByIngredient(ingredient: String): List<DrinkPreviewModel> {
        val response = service.searchByIngredient(ingredient)
        return previewMapper.apply(response)
    }
}