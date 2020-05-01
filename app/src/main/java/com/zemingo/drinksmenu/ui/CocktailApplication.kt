package com.zemingo.drinksmenu.ui

import android.app.Application
import android.content.Context
import com.zemingo.drinksmenu.di.KoinStarter
import com.zemingo.drinksmenu.domain.FetchIngredientsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

class CocktailApplication : Application() {

    private val koinStarter = KoinStarter()

    /*companion object {
        private lateinit var appCtx: Context
        val applicationContext: Context = appCtx
    }*/

    override fun onCreate() {
        super.onCreate()
//        appCtx = this
        koinStarter.start(this)
        Timber.plant(Timber.DebugTree())
        GlobalScope.launch(Dispatchers.IO) {
            val fetchIngredientsUseCase: FetchIngredientsUseCase by inject()
            fetchIngredientsUseCase.fetch()
        }
    }
}