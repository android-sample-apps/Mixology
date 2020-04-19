package com.zemingo.cocktailmenu.di

import com.zemingo.cocktailmenu.domain.GetCategoriesUseCase
import org.koin.dsl.module

val useCasesModule = module {

    factory { GetCategoriesUseCase(get()) }
}