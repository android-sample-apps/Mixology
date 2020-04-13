package com.zemingo.cocktailmenu.ui

import android.app.Application
import com.zemingo.cocktailmenu.di.KoinStarter

class CocktailApplication : Application() {

    private val koinStarter = KoinStarter()

    override fun onCreate() {
        super.onCreate()
        koinStarter.start(this)
    }
}