package com.zemingo.cocktailmenu.di

import com.zemingo.cocktailmenu.view_model.CategoriesViewModel
import com.zemingo.cocktailmenu.view_model.mappers.CategoryMapperUi
import com.zemingo.cocktailmenu.view_model.mappers.DrinkMapperUi
import com.zemingo.cocktailmenu.view_model.mappers.DrinkPreviewMapperUi
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