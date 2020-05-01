package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.repo.repositories.DrinkPreviewRepository
import kotlinx.coroutines.flow.Flow

class GetLatestArrivalsUseCase(
    repository: DrinkPreviewRepository
) {

    val latestArrivals: Flow<List<DrinkPreviewModel>> =
        repository
            .latestArrivals()
}