package com.zemingo.cocktailmenu.di

import com.zemingo.cocktailmenu.repo.CocktailService
import com.zemingo.cocktailmenu.repo.mappers.CategoryMapper
import com.zemingo.cocktailmenu.repo.mappers.DrinkMapper
import com.zemingo.cocktailmenu.repo.mappers.DrinkPreviewMapper
import com.zemingo.cocktailmenu.repo.mappers.IngredientsMapper
import com.zemingo.cocktailmenu.repo.reactive_store.CategoryReactiveStore
import com.zemingo.cocktailmenu.repo.repositories.CategoryRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("RemoveExplicitTypeArguments")
val repoModule = module {

    factory<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    single<CocktailService> {
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://www.thecocktaildb.com/api/json/v1/1/")
            .build().create(CocktailService::class.java)
    }


    factory {
        CategoryRepository(
            service = get<CocktailService>(),
            reactiveStore = get<CategoryReactiveStore>(),
            mapper = get<CategoryMapper>()
        )
    }


}