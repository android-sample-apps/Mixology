package com.yanivsos.mixological.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.yanivsos.mixological.BuildConfig
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit

@ExperimentalSerializationApi
@Suppress("RemoveExplicitTypeArguments")
val networkModule = module {

    factory<HttpLoggingInterceptor.Level> {
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
    }

    factory<OkHttpClient> {
        OkHttpClient
            .Builder()
            .addInterceptor(TrafficStateInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = get<HttpLoggingInterceptor.Level>()
            })
            .build()
    }

    single<Converter.Factory> {
        Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
            isLenient = true
        }.asConverterFactory("application/json".toMediaType())
    }

    single<DrinkService> {
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .addConverterFactory(get())
            .baseUrl("https://www.thecocktaildb.com/api/json/v2/9973533/")
            .build().create(DrinkService::class.java)
    }
}
