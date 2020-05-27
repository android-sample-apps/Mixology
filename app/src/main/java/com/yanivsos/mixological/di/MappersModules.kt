package com.yanivsos.mixological.di

import com.yanivsos.mixological.repo.mappers.*
import com.yanivsos.mixological.ui.mappers.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mappersModule = module {

    factory { DrinkPreviewMapperUi() }

    factory { DrinkPreviewMapper() }

    factory { CategoryMapperUi() }

    factory { CategoryMapper() }

    factory { IngredientMapper() }

    factory { DrinkMapper(get<SingleDrinkMapper>()) }

    factory { (drinkId: String) ->
        ResultDrinkMapperUi(
            drinkMapper = get<DrinkMapperUi>(),
            drinkId = drinkId
        )
    }

    factory { SearchDrinkMapper(get<SingleDrinkMapper>()) }

    factory { SearchDrinkPreviewMapper(get<SingleDrinkMapper>()) }

    factory { SingleDrinkMapper() }


    factory { DrinkMapperUi(androidContext()) }

    factory {
        DrinkMapperListUi(
            singleMapper = get<DrinkMapperUi>()
        )
    }

    factory { IngredientDetailsMapper() }

    factory { IngredientDetailsMapperUi() }

    factory { GlassMapper() }

    factory { GlassMapperUi() }

    factory { AlcoholicFilterMapper() }

    factory { AlcoholicFilterMapperUi() }

    factory { IngredientFilterMapperUi() }

    factory {
        SearchFiltersMapperUi()
    }

}