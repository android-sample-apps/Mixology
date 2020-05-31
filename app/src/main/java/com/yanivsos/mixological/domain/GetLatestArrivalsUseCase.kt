package com.yanivsos.mixological.domain

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.repo.repositories.LatestArrivalsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import timber.log.Timber

class GetLatestArrivalsUseCase(
    getDrinkPreviewUseCase: GetDrinkPreviewUseCase,
    repository: LatestArrivalsRepository
) {

    val latestArrivals: Flow<List<DrinkPreviewModel>> =
        repository
            .getAll()
            .map { latestArrivals -> latestArrivals.map { it.drinkId } }
            .flatMapMerge {
                Timber.d("received: ids[$it]")
                getDrinkPreviewUseCase.getByIds(it)
            }
}