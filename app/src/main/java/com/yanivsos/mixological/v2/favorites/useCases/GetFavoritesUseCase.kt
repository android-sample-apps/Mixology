package com.yanivsos.mixological.v2.favorites.useCases

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.v2.drink.repo.DrinkRepository
import kotlinx.coroutines.flow.Flow

class GetFavoritesUseCase(
    repository: DrinkRepository
) {
    val favorites: Flow<List<DrinkPreviewModel>> = repository.getFavorites()
}
