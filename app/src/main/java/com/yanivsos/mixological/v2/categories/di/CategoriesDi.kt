package com.yanivsos.mixological.v2.categories.di

import com.yanivsos.mixological.repo.room.DrinksDatabase
import com.yanivsos.mixological.v2.categories.repo.CategoriesRepository
import com.yanivsos.mixological.v2.categories.useCases.GetCategoriesStateUseCase
import com.yanivsos.mixological.v2.categories.useCases.GetDrinksByCategoryUseCase
import com.yanivsos.mixological.v2.categories.viewModel.CategoriesViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val categoriesDi = module {

    single { get<DrinksDatabase>().getCategoriesDao() }

    single {
        CategoriesRepository(
            categoriesDao = get(),
            drinkService = get()
        )
    }

    factory {
        GetDrinksByCategoryUseCase(
            categoriesRepository = get(),
            drinkRepository = get()
        )
    }

    factory {
        GetCategoriesStateUseCase(
            categoriesRepository = get(),
            getDrinksByCategoryUseCase = get()
        )
    }

    viewModel {
        CategoriesViewModel(
            getCategoriesStateUseCase = get()
        )
    }
}
