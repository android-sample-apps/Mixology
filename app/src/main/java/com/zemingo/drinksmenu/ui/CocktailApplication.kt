package com.zemingo.drinksmenu.ui

import android.app.Application
import com.zemingo.drinksmenu.di.KoinStarter
import com.zemingo.drinksmenu.domain.FetchIngredientsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

class CocktailApplication : Application() {

    private val koinStarter = KoinStarter()

    override fun onCreate() {
        super.onCreate()
        koinStarter.start(this)
        Timber.plant(Timber.DebugTree())
        GlobalScope.launch(Dispatchers.IO) {
            val fetchIngredientsUseCase: FetchIngredientsUseCase by inject()
            fetchIngredientsUseCase.fetch()
        }
    }
}