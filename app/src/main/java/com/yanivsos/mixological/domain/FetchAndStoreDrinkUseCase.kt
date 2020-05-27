package com.yanivsos.mixological.domain

import com.yanivsos.mixological.repo.repositories.DrinkRepository
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