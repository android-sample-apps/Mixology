package com.zemingo.cocktailmenu.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class KoinStarter {

    fun start(app: Application) {
        startKoin {
            androidContext(app)
            modules(myModules())
        }
    }

    private fun myModules(): List<Module> {
        return listOf(
            repoModule,
            reactiveStoreModule,
            mappersModule,
            useCasesModule,
            viewModelModule
        )
    }
}