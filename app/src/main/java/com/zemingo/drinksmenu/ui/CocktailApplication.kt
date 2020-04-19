package com.zemingo.drinksmenu.ui

import android.app.Application
import com.zemingo.drinksmenu.di.KoinStarter

class CocktailApplication : Application() {

    private val koinStarter = KoinStarter()

    override fun onCreate() {
        super.onCreate()
        koinStarter.start(this)
    }
}