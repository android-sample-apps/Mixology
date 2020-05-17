package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.repo.repositories.DrinkPreviewRepository
import kotlinx.coroutines.flow.Flow

class GetDrinkPreviewUseCase(
    private val repository: DrinkPreviewRepository,
    private val combineWithFavoriteUseCase: CombineWithFavoriteUseCase
) {
    fun getAll(): Flow<List<DrinkPreviewModel>> =
        combineWithFavoriteUseCase.combine(repository.getAll())


    fun getById(id: String) =
        combineWithFavoriteUseCase.combine(repository.getByIds(listOf(id)))
}