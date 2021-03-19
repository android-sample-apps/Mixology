package com.yanivsos.mixological.v2.drink.useCases

import com.yanivsos.mixological.v2.drink.repo.DrinkRepository

class FetchAndStoreDrinkUseCase(
    private val repo: DrinkRepository
) {
    suspend fun fetchAndStore(drinkId: String) {
        val drink = repo.fetchById(drinkId)
        requireNotNull(drink)
        repo.store(drink)
    }
}
