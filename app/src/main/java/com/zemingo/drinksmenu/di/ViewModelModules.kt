package com.zemingo.drinksmenu.di

import com.zemingo.drinksmenu.view_model.CategoriesViewModel
import com.zemingo.drinksmenu.view_model.mappers.CategoryMapperUi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        CategoriesViewModel(
            categoriesUseCase = get(),
            mapper = get<CategoryMapperUi>()
        )
    }
}