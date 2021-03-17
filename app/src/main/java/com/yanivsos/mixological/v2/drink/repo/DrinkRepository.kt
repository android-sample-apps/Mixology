package com.yanivsos.mixological.v2.drink.repo

import com.yanivsos.mixological.domain.models.DrinkModel
import com.yanivsos.mixological.repo.DrinkService
import com.yanivsos.mixological.v2.drink.dao.DrinkDao
import com.yanivsos.mixological.v2.drink.dao.FavoriteDrinksDao
import com.yanivsos.mixological.v2.drink.mappers.toFirstOrNullModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext

class DrinkRepository(
    private val drinkService: DrinkService,
    private val drinkDao: DrinkDao,
    private val favoriteDrinksDao: FavoriteDrinksDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun fetchById(id: String): DrinkModel? {
        return withContext(ioDispatcher) {
            drinkService
                .getDrinkById(id)
                .toFirstOrNullModel()
        }
    }

    suspend fun store(drinkModel: DrinkModel) {
        withContext(ioDispatcher) {
            drinkDao.store(drinkModel)
        }
    }

    fun getById(id: String): Flow<DrinkModel?> {
        return drinkDao
            .getById(id)
            .combine(favoriteDrinksDao.getById(id)) { drink, watchlist ->
                drink?.copy(isFavorite = watchlist != null)
            }.distinctUntilChanged()
    }

}
