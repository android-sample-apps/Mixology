package com.zemingo.drinksmenu.ui

import android.app.Application
import com.zemingo.drinksmenu.di.KoinStarter
import timber.log.Timber

@Suppress("unused")
class CocktailApplication : Application() {

    private val koinStarter = KoinStarter()

    override fun onCreate() {
        super.onCreate()
        koinStarter.start(this)
        Timber.plant(Timber.DebugTree())
    }
}