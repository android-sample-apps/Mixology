package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.repo.repositories.DrinkRepository

class GetDrinkUseCase(
    private val repository: DrinkRepository
) {
    fun getDrink(id: String) = repository.get(id)
}