package com.zemingo.drinksmenu.repo.repositories

import com.zemingo.drinksmenu.models.DrinkModel
import com.zemingo.drinksmenu.models.DrinkResponse
import com.zemingo.drinksmenu.models.DrinksWrapperResponse
import com.zemingo.drinksmenu.repo.CocktailService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.function.Function

class DrinkRepository(
    private val service: CocktailService,
    private val mapper: Function<DrinksWrapperResponse<DrinkResponse>, DrinkModel>
) {
    private suspend fun getById(id: String): DrinkModel {
        val response = service.getDrinkById(id)
        return mapper.apply(response)
    }

    fun get(id: String): Flow<DrinkModel> {
        return flow {
            emit(getById(id))
        }
    }
}