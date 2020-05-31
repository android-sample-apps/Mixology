package com.yanivsos.mixological.domain

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.repo.repositories.MostPopularRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map

class GetMostPopularUseCase(
    getDrinkPreviewUseCase: GetDrinkPreviewUseCase,
    repository: MostPopularRepository
) {
    val mostPopular: Flow<List<DrinkPreviewModel>> =
        repository
            .getAll()
            .map { mostPopular -> mostPopular.map { it.drinkId } }
            .flatMapMerge {
                getDrinkPreviewUseCase.getByIds(it)
            }

}