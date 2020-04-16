package com.zemingo.cocktailmenu.di

import com.zemingo.cocktailmenu.view_model.DrinksViewModel
import com.zemingo.cocktailmenu.view_model.mappers.DrinkMapperUi
import com.zemingo.cocktailmenu.view_model.mappers.DrinkPreviewMapperUi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
//import java.util.function.Function

val viewModelModule = module {

    factory/*<Function<DrinkModel, DrinkItemUiModel>>*/ { DrinkMapperUi() }

    factory/*<Function<DrinkPreviewListModel, DrinksPreviewListItemUiModel>> */{ DrinkPreviewMapperUi() }

    viewModel {
        DrinksViewModel(
            drinksRepository = get(),
            drinkMapper = get<DrinkMapperUi>(),
            drinkPreviewMapper = get<DrinkPreviewMapperUi>()
        )
    }
}