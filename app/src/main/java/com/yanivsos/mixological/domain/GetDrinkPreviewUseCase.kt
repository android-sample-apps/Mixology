package com.yanivsos.mixological.domain

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.repo.repositories.DrinkPreviewRepository
import kotlinx.coroutines.flow.Flow

class GetDrinkPreviewUseCase(
    private val repository: DrinkPreviewRepository,
    private val combineWithFavoriteUseCase: CombineWithFavoriteUseCase
) {
    fun getAll(): Flow<List<DrinkPreviewModel>> =
        combineWithFavoriteUseCase.combine(repository.getAll())

    fun getById(id: String) =
        combineWithFavoriteUseCase.combine(repository.getByIds(listOf(id)))

    fun getByIds(ids: List<String>) =
        combineWithFavoriteUseCase.combine(repository.getByIds(ids))
}