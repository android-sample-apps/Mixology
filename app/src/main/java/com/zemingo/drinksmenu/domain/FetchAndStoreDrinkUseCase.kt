package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.repo.repositories.DrinkRepository
import timber.log.Timber

class FetchAndStoreDrinkUseCase(
    private val repository: DrinkRepository
) {
    suspend fun fetchAndStore(drinkId: String) {
        Timber.d("fetchAndStore: called with id[$drinkId]")
        repository.run {
            val drink = fetch(drinkId)
            store(drink)
            Timber.d("fetchAndStore. finished")
        }
    }
}