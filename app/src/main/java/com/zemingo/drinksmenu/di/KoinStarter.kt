package com.zemingo.drinksmenu.di

import android.app.Application
import com.zemingo.drinksmenu.domain.di.useCasesModule
import com.zemingo.drinksmenu.repo.di.reactiveStoreModule
import com.zemingo.drinksmenu.repo.di.repoModule
import com.zemingo.drinksmenu.ui.di.viewModelModule
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