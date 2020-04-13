package com.zemingo.cocktailmenu.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KoinStarter {

    fun start(app: Application) {
        startKoin {
            androidContext(app)
            modules(listOf(
                repoModule
            ))
        }
    }
}