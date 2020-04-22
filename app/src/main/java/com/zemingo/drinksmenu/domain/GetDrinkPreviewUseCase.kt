package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.repo.repositories.DrinkPreviewRepository

class GetDrinkPreviewUseCase(
    private val repo: DrinkPreviewRepository
) {
    fun getAll() = repo.getAll()
}