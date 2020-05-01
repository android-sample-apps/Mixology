package com.zemingo.drinksmenu.repo.reactive_store

import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.repo.room.DrinkPreviewDao
import kotlinx.coroutines.flow.Flow

class DrinkPreviewReactiveStore(
    private val drinkPreviewDao: DrinkPreviewDao
) : ReactiveStore<String, DrinkPreviewModel, DrinkPreviewParam> {

    override fun storeAll(data: List<DrinkPreviewModel>) {
        drinkPreviewDao.insertAll(data)
    }

    override fun getAll(key: List<String>?): Flow<List<DrinkPreviewModel>> {
        return key?.let { drinkPreviewDao.getByIds(it) } ?: drinkPreviewDao.getAll()
    }

    override fun getByParam(param: DrinkPreviewParam): Flow<List<DrinkPreviewModel>> {
        return when (param) {
            DrinkPreviewParam.SearchHistory -> drinkPreviewDao.getPreviousSearches()
        }
    }

}

sealed class DrinkPreviewParam {
    object SearchHistory : DrinkPreviewParam()
}