package com.zemingo.drinksmenu.repo.reactive_store

import com.zemingo.drinksmenu.models.DrinkPreviewModel
import com.zemingo.drinksmenu.room.DrinkPreviewDao
import kotlinx.coroutines.flow.Flow

class DrinkPreviewReactiveStore(
    private val drinkPreviewDao: DrinkPreviewDao
) : ReactiveStore<DrinkPreviewModel> {

    override fun getAll(): Flow<List<DrinkPreviewModel>> {
        return drinkPreviewDao.getAll()
    }

    override fun storeAll(data: List<DrinkPreviewModel>) {
        drinkPreviewDao.insertAll(data)
    }
}