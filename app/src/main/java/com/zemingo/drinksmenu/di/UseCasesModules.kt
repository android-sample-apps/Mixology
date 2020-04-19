package com.zemingo.drinksmenu.di

import com.zemingo.drinksmenu.domain.GetCategoriesUseCase
import org.koin.dsl.module

val useCasesModule = module {

    factory { GetCategoriesUseCase(get()) }
}