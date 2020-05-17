package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.repo.repositories.DrinkPreviewRepository
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