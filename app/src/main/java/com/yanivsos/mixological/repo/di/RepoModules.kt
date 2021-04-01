package com.yanivsos.mixological.repo.di

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.yanivsos.mixological.BuildConfig
import com.yanivsos.mixological.network.FilterResponseDeserializer
import com.yanivsos.mixological.repo.DrinkService
import com.yanivsos.mixological.repo.mappers.*
import com.yanivsos.mixological.repo.models.DrinkPreviewResponse
import com.yanivsos.mixological.repo.models.DrinksWrapperResponse
import com.yanivsos.mixological.repo.reactiveStore.*
import com.yanivsos.mixological.repo.repositories.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Suppress("RemoveExplicitTypeArguments")
val repoModule = module {

    factory<HttpLoggingInterceptor.Level> {
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
    }

    factory<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = get<HttpLoggingInterceptor.Level>()
            })
            .build()
    }

    single<Converter.Factory> {
        GsonConverterFactory
            .create(
                GsonBuilder()
                    .registerTypeAdapter(
                        object : TypeToken<DrinksWrapperResponse<DrinkPreviewResponse>>() {}.type,
                        FilterResponseDeserializer()
                    )
                    .create()
            )
    }

    single<DrinkService> {
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .addConverterFactory(get())
            .baseUrl("https://www.thecocktaildb.com/api/json/v2/9973533/")
            .build().create(DrinkService::class.java)
    }


    factory {
        CategoryRepository(
            service = get<DrinkService>(),
            reactiveStore = get<CategoryReactiveStore>(),
            mapper = get<CategoryMapper>()
        )
    }

    factory {
        IngredientRepository(
            service = get<DrinkService>(),
            reactiveStore = get<IngredientReactiveStore>(),
            mapper = get<IngredientMapper>()
        )
    }

    factory {
        DrinkPreviewRepository(
            service = get<DrinkService>(),
            reactiveStore = get<DrinkPreviewReactiveStore>(),
            mapper = get<DrinkPreviewMapper>(),
            searchMapper = get<SearchDrinkPreviewMapper>()
        )
    }

    factory {
        RecentlyViewedRepository(
            recentlyViewedReactiveStore = get<RecentlyViewedReactiveStore>(),
            drinkReactiveStore = get<DrinkPreviewReactiveStore>()
        )
    }

    factory {
        DrinkRepository(
            reactiveStore = get<DrinkReactiveStore>(),
            service = get<DrinkService>(),
            mapper = get<DrinkMapper>()
        )
    }

    factory {
        IngredientDetailsRepository(
            service = get<DrinkService>(),
            reactiveStore = get<IngredientDetailsReactiveStore>(),
            mapper = get<IngredientDetailsMapper>()
        )
    }

    factory {
        AdvancedSearchRepository(
            service = get<DrinkService>(),
            drinkReactiveStore = get<DrinkReactiveStore>(),
            drinkMapper = get<SearchDrinkMapper>(),
            previewMapper = get<DrinkPreviewMapper>()

        )
    }

    factory {
        GlassRepository(
            service = get<DrinkService>(),
            reactiveStore = get<GlassReactiveStore>(),
            mapper = get<GlassMapper>()
        )
    }

    factory {
        AlcoholicFilterRepository(
            service = get<DrinkService>(),
            reactiveStore = get<AlcoholicFiltersReactiveStore>(),
            mapper = get<AlcoholicFilterMapper>()
        )
    }

    factory {
        WatchlistRepository(
            reactiveStore = get<WatchlistReactiveStore>()
        )
    }

    factory {
        LatestArrivalsRepository(
            service = get<DrinkService>(),
            reactiveStore = get<LatestArrivalsReactiveStore>(),
            mapper = get<DrinkPreviewMapper>()
        )
    }

    factory {
        MostPopularRepository(
            service = get<DrinkService>(),
            reactiveStore = get<MostPopularReactiveStore>(),
            mapper = get<DrinkPreviewMapper>()
        )
    }
}
