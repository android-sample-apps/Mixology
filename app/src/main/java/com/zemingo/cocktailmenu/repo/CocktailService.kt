package com.zemingo.cocktailmenu.repo

import com.zemingo.cocktailmenu.models.DrinksListResponse
import com.zemingo.cocktailmenu.models.FullDrinksListResponse
import retrofit2.http.GET

interface CocktailService {
    @GET("random.php")
    suspend fun random(): FullDrinksListResponse

    @GET("filter.php?c=Cocktail")
    suspend fun getCocktails(): DrinksListResponse
}