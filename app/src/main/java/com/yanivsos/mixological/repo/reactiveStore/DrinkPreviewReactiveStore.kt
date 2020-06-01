package com.yanivsos.mixological.repo.reactiveStore

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.repo.room.DrinkPreviewDao
import kotlinx.coroutines.flow.Flow

class DrinkPreviewReactiveStore(
    private val drinkPreviewDao: DrinkPreviewDao
) : NonRemovableReactiveStore<DrinkPreviewModel, DrinkPreviewParam> {

    override fun storeAll(data: List<DrinkPreviewModel>) {
        drinkPreviewDao.insertAll(data)
    }

    override fun get(param: DrinkPreviewParam): Flow<List<DrinkPreviewModel>> {
        return when (param) {
            is DrinkPreviewParam.All -> drinkPreviewDao.getAll()
            is DrinkPreviewParam.SearchHistory -> drinkPreviewDao.getPreviousSearches()
            is DrinkPreviewParam.ByIds -> drinkPreviewDao.getByIds(param.ids)
        }
    }
}

sealed class DrinkPreviewParam {
    object All : DrinkPreviewParam()
    object SearchHistory : DrinkPreviewParam()
    data class ByIds(val ids: List<String>) : DrinkPreviewParam()
}