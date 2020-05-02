package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.repo.repositories.DrinkRepository
import kotlinx.coroutines.flow.Flow

class GetDrinkUseCase(
    repository: DrinkRepository,
    id: String
) {
    val drink: Flow<DrinkModel> =
        repository
            .get(id)
}