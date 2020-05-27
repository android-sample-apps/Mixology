package com.yanivsos.mixological.domain

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.repo.repositories.DrinkPreviewRepository
import kotlinx.coroutines.flow.Flow

class GetMostPopularUseCase(
    combineWithFavoriteUseCase: CombineWithFavoriteUseCase,
    repository: DrinkPreviewRepository
) {
    val mostPopular: Flow<List<DrinkPreviewModel>> =
        combineWithFavoriteUseCase.combine(
            repository
                .mostPopular()
        )

}