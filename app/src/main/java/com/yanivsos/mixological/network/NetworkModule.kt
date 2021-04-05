package com.yanivsos.mixological.network

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.yanivsos.mixological.BuildConfig
import com.yanivsos.mixological.network.response.DrinkPreviewResponse
import com.yanivsos.mixological.network.response.DrinksWrapperResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("RemoveExplicitTypeArguments")
val networkModule = module {

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
}
