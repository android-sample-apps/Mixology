package com.zemingo.cocktailmenu.repo

import com.zemingo.cocktailmenu.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.function.Function

class CocktailRepository(
    private val cocktailService: CocktailService,
    private val drinkMapper: Function<FullDrinkResponse, DrinkModel>,
    private val drinkPreviewMapper: Function<DrinksListResponse, DrinkPreviewListModel>
) {
    suspend fun random(): Flow<DrinkModel> {
        return flow {
            val response = cocktailService.random().drinks.first()
            val model = drinkMapper.apply(response)
            emit(model)
        }.flowOn(Dispatchers.IO)
    }


    suspend fun cocktails(): Flow<DrinkPreviewListModel> {
        return flow {
            val response = cocktailService.getCocktails()
            val model = drinkPreviewMapper.apply(response)
            emit(model)
        }.flowOn(Dispatchers.IO)
    }
}