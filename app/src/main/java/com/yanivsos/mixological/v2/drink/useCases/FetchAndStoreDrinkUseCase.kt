package com.yanivsos.mixological.v2.drink.useCases

import com.yanivsos.mixological.v2.drink.DrinkRepository
import timber.log.Timber

class FetchAndStoreDrinkUseCase(
    private val repo: DrinkRepository
) {

    suspend fun fetchAndStore(drinkId: String) {
        Timber.i("fetchAndStore: drinkId[$drinkId]")
        val drink = repo.fetchById(drinkId)
        requireNotNull(drink)
        repo.store(drink)
    }
}
