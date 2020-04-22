package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.repo.repositories.DrinkPreviewRepository

class GetDrinkPreviewUseCase(
    private val repository: DrinkPreviewRepository
) {
    fun getAll() = repository.getAll()
}