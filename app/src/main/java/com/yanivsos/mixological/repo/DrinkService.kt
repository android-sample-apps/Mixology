package com.yanivsos.mixological.repo

import com.yanivsos.mixological.repo.models.*
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *

Search cocktail by name
https://www.thecocktaildb.com/api/json/v1/1/search.php?s=margarita

List all cocktails by first letter
https://www.thecocktaildb.com/api/json/v1/1/search.php?f=a

Search ingredient by name
https://www.thecocktaildb.com/api/json/v1/1/search.php?i=vodka

Lookup full cocktail details by id
https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=11007

Lookup ingredient by ID
https://www.thecocktaildb.com/api/json/v1/1/lookup.php?iid=552

Lookup a random cocktail
https://www.thecocktaildb.com/api/json/v1/1/random.php

Lookup a selection of 10 random cocktails (only available to $2+ Patreon supporters)
https://www.thecocktaildb.com/api/json/v1/1/randomselection.php

List Popular cocktails (only available to $2+ Patreon supporters)
https://www.thecocktaildb.com/api/json/v1/1/popular.php

List most latest cocktails (only available to $2+ Patreon supporters)
https://www.thecocktaildb.com/api/json/v1/1/latest.php

Search by ingredient
https://www.thecocktaildb.com/api/json/v1/1/filter.php?i=Gin
https://www.thecocktaildb.com/api/json/v1/1/filter.php?i=Vodka

Filter by multi-ingredient (only available to $2+ Patreon supporters)
https://www.thecocktaildb.com/api/json/v1/1/filter.php?i=Dry_Vermouth,Gin,Anis

Filter by alcoholic
https://www.thecocktaildb.com/api/json/v1/1/filter.php?a=Alcoholic
https://www.thecocktaildb.com/api/json/v1/1/filter.php?a=Non_Alcoholic

Filter by Category
https://www.thecocktaildb.com/api/json/v1/1/filter.php?c=Ordinary_Drink
https://www.thecocktaildb.com/api/json/v1/1/filter.php?c=Cocktail

Filter by Glass
https://www.thecocktaildb.com/api/json/v1/1/filter.php?g=Cocktail_glass
https://www.thecocktaildb.com/api/json/v1/1/filter.php?g=Champagne_flute

List the categories, glasses, ingredients or alcoholic filters
https://www.thecocktaildb.com/api/json/v1/1/list.php?c=list
https://www.thecocktaildb.com/api/json/v1/1/list.php?g=list
https://www.thecocktaildb.com/api/json/v1/1/list.php?i=list
https://www.thecocktaildb.com/api/json/v1/1/list.php?a=list

 * */

interface DrinkService {

//    @GET("random.php")
//    suspend fun random(): DrinksWrapperResponse<FullDrinkResponse>

    @GET("list.php?i=list")
    suspend fun getIngredientList(): DrinksWrapperResponse<IngredientResponse>

    @GET("list.php?c=list")
    suspend fun getCategoryList(): DrinksWrapperResponse<CategoryResponse>

    @GET("list.php?a=list")
    suspend fun getAlcoholicFilterList(): DrinksWrapperResponse<AlcoholicFilterResponse>

    @GET("list.php?g=list")
    suspend fun getGlassList(): DrinksWrapperResponse<GlassResponse>

    @GET("filter.php")
    suspend fun filterByCategory(@Query("c") category: String): DrinksWrapperResponse<DrinkPreviewResponse>

    @GET("filter.php")
    suspend fun filterByIngredient(@Query("i") ingredient: String): DrinksWrapperResponse<DrinkPreviewResponse>

    @GET("filter.php")
    suspend fun filterByAlcoholic(@Query("a") alcoholic: String): DrinksWrapperResponse<DrinkPreviewResponse>

    @GET("filter.php")
    suspend fun filterByGlass(@Query("g") glass: String): DrinksWrapperResponse<DrinkPreviewResponse>

    @GET("lookup.php")
    suspend fun getDrinkById(@Query("i") id: String): DrinksWrapperResponse<DrinkResponse>

    @GET("search.php")
    suspend fun getIngredientByName(@Query("i") name: String): IngredientsWrapperResponse<IngredientDetailsResponse>

    @GET("search.php")
    suspend fun searchByName(@Query("s") name: String): NullableDrinksWrapperResponse<DrinkResponse>

    @GET("search.php")
    suspend fun searchByFirstLetter(@Query("f") firstLetter: String): NullableDrinksWrapperResponse<DrinkResponse>

    @GET("latest.php")
    suspend fun getLatestArrivals(): DrinksWrapperResponse<DrinkPreviewResponse>

    @GET("popular.php")
    suspend fun getMostPopular(): DrinksWrapperResponse<DrinkPreviewResponse>
}