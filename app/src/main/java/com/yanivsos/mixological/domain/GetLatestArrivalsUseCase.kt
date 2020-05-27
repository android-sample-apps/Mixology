package com.yanivsos.mixological.domain

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.repo.repositories.DrinkPreviewRepository
import kotlinx.coroutines.flow.Flow

class GetLatestArrivalsUseCase(
    combineWithFavoriteUseCase: CombineWithFavoriteUseCase,
    repository: DrinkPreviewRepository
) {

    val latestArrivals: Flow<List<DrinkPreviewModel>> =
        combineWithFavoriteUseCase.combine(repository
            .latestArrivals())
}