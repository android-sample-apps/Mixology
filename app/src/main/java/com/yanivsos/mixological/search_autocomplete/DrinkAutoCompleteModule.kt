package com.yanivsos.mixological.search_autocomplete

import org.koin.dsl.module

val drinkAutoCompleteModule = module {

    factory {
        GetDrinkAutoCompleteUseCase(get(), get())
    }

    factory {
        DrinkAutoCompleteMapper()
    }

    factory {
        DrinkListAutoCompleteMapper(get())
    }
}