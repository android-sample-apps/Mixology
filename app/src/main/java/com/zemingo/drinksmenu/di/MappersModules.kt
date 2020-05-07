package com.zemingo.drinksmenu.di

import com.zemingo.drinksmenu.repo.mappers.*
import com.zemingo.drinksmenu.ui.mappers.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mappersModule = module {

    factory { DrinkPreviewMapperUi() }

    factory { DrinkPreviewMapper() }

    factory { CategoryMapperUi() }

    factory { CategoryMapper() }

    factory { IngredientMapper() }

    factory { DrinkMapper(get<SingleDrinkMapper>()) }

    factory { DrinkToPreviewUiMapper() }

    factory { SearchDrinkMapper(get<SingleDrinkMapper>()) }

    factory { SingleDrinkMapper() }

    factory { DrinkMapperUi(androidContext()) }

    factory {
        DrinkMapperListUi(
            singleMapper = get<DrinkMapperUi>()
        )
    }

    factory { IngredientDetailsMapper() }

    factory { IngredientDetailsMapperUi() }

}