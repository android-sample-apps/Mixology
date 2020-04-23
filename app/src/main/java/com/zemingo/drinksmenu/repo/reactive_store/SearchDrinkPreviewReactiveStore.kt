package com.zemingo.drinksmenu.repo.reactive_store

import com.zemingo.drinksmenu.models.PreviousSearchModel
import com.zemingo.drinksmenu.room.SearchDrinkPreviewDao
import kotlinx.coroutines.flow.Flow

class SearchDrinkPreviewReactiveStore(
    private val searchDrinkPreviewDao: SearchDrinkPreviewDao
) : ReactiveStore<PreviousSearchModel> {

    override fun getAll(): Flow<List<PreviousSearchModel>> {
        return searchDrinkPreviewDao.getAll()
    }

    override fun storeAll(data: List<PreviousSearchModel>) {
        searchDrinkPreviewDao.insertAll(data)
    }

    /*fun getAllWithDrinks(): Flow<List<DrinkPreviewModel>> {
        return searchDrinkPreviewDao.getHistory()
    }*/
}