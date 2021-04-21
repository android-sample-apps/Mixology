package com.yanivsos.mixological.di

import android.content.Context
import com.yanivsos.mixological.database.databaseModule
import com.yanivsos.mixological.network.networkModule
import com.yanivsos.mixological.ui.di.viewModelModule
import com.yanivsos.mixological.v2.categories.di.categoriesDi
import com.yanivsos.mixological.v2.drink.di.drinkDi
import com.yanivsos.mixological.v2.drinkOptions.di.drinkOptionsDi
import com.yanivsos.mixological.v2.favorites.di.favoriteDi
import com.yanivsos.mixological.v2.inAppReview.inAppReviewModule
import com.yanivsos.mixological.v2.ingredients.di.ingredientsDi
import com.yanivsos.mixological.v2.landingPage.di.landingPageDi
import com.yanivsos.mixological.v2.search.di.searchDi
import com.yanivsos.mixological.v2.settings.settingsDi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class KoinStarter {

    fun start(context: Context) {
        startKoin {
            androidContext(context)
            modules(myModules())
        }
    }

    private fun myModules(): List<Module> {
        return listOf(
            networkModule,
            databaseModule,
            inAppReviewModule,
            viewModelModule,
            drinkDi,
            favoriteDi,
            landingPageDi,
            categoriesDi,
            ingredientsDi,
            drinkOptionsDi,
            searchDi,
            settingsDi
        )
    }
}
