package com.yanivsos.mixological.v2.drink.repo

import com.yanivsos.mixological.domain.models.DrinkModel
import com.yanivsos.mixological.repo.DrinkService
import com.yanivsos.mixological.v2.drink.dao.DrinkDao
import com.yanivsos.mixological.v2.drink.dao.FavoriteDrinksDao
import com.yanivsos.mixological.v2.drink.mappers.toFirstOrNullModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class DrinkRepository(
    private val drinkService: DrinkService,
    private val drinkDao: DrinkDao,
    private val favoriteDrinksDao: FavoriteDrinksDao
) {
    suspend fun fetchById(id: String): DrinkModel? {
        return drinkService
            .getDrinkById(id)
            .toFirstOrNullModel()
    }

    fun getById(id: String): Flow<DrinkModel?> {
        return drinkDao
            .getById(id)
            .combine(favoriteDrinksDao.getById(id)) { drink, watchlist ->
                drink?.copy(isFavorite = watchlist != null)
            }
    }

    suspend fun store(drinkModel: DrinkModel) {
        drinkDao.store(drinkModel)
    }
}
