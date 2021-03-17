package com.yanivsos.mixological.di

import android.content.Context
import com.yanivsos.mixological.domain.di.useCasesModule
import com.yanivsos.mixological.in_app_review.inAppReviewModule
import com.yanivsos.mixological.repo.di.reactiveStoreModule
import com.yanivsos.mixological.repo.di.repoModule
import com.yanivsos.mixological.search_autocomplete.drinkAutoCompleteModule
import com.yanivsos.mixological.ui.di.viewModelModule
import com.yanivsos.mixological.v2.drink.di.drinkDi
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
            repoModule,
            reactiveStoreModule,
            mappersModule,
            useCasesModule,
            viewModelModule,
            inAppReviewModule,
            drinkAutoCompleteModule,
            drinkDi
        )
    }
}
