package com.zemingo.drinksmenu.repo.reactive_store

import com.zemingo.drinksmenu.domain.models.PreviousSearchModel
import com.zemingo.drinksmenu.repo.room.SearchDrinkPreviewDao
import kotlinx.coroutines.flow.Flow

class SearchDrinkPreviewReactiveStore(
    private val searchDrinkPreviewDao: SearchDrinkPreviewDao
) : ReactiveStore<String, PreviousSearchModel, Void> {

    override fun getAll(key: List<String>?): Flow<List<PreviousSearchModel>> {
        return key?.let { searchDrinkPreviewDao.getAll(it) } ?: searchDrinkPreviewDao.getAll()
    }

    override fun storeAll(data: List<PreviousSearchModel>) {
        searchDrinkPreviewDao.insertAll(data)
    }

    override fun getByParam(param: Void): Flow<List<PreviousSearchModel>> {
        TODO("Not yet implemented")
    }

    override fun remove(key: String) {
        TODO("Not yet implemented")
    }
}
