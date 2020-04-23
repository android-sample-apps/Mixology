package com.zemingo.drinksmenu.di

import com.zemingo.drinksmenu.repo.CocktailService
import com.zemingo.drinksmenu.repo.mappers.CategoryMapper
import com.zemingo.drinksmenu.repo.mappers.DrinkMapper
import com.zemingo.drinksmenu.repo.mappers.DrinkPreviewMapper
import com.zemingo.drinksmenu.repo.mappers.IngredientMapper
import com.zemingo.drinksmenu.repo.reactive_store.CategoryReactiveStore
import com.zemingo.drinksmenu.repo.reactive_store.DrinkPreviewReactiveStore
import com.zemingo.drinksmenu.repo.reactive_store.IngredientReactiveStore
import com.zemingo.drinksmenu.repo.reactive_store.SearchDrinkPreviewReactiveStore
import com.zemingo.drinksmenu.repo.repositories.*
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

    factory {
        IngredientRepository(
            service = get<CocktailService>(),
            reactiveStore = get<IngredientReactiveStore>(),
            mapper = get<IngredientMapper>()
        )
    }

    factory {
        DrinkPreviewRepository(
            service = get<CocktailService>(),
            reactiveStore = get<DrinkPreviewReactiveStore>(),
            mapper = get<DrinkPreviewMapper>()
        )
    }

    factory {
        SearchDrinkPreviewRepository(
            searchReactiveStore = get<SearchDrinkPreviewReactiveStore>(),
            previewReactiveStore = get<DrinkPreviewReactiveStore>()
        )
    }

    factory {
        DrinkRepository(
            service = get<CocktailService>(),
            mapper = get<DrinkMapper>()
        )
    }
}