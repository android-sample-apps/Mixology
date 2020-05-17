package com.zemingo.drinksmenu.repo.repositories

import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.domain.models.RecentlyViewedModel
import com.zemingo.drinksmenu.repo.reactive_store.DrinkPreviewParam
import com.zemingo.drinksmenu.repo.reactive_store.ReactiveStore
import kotlinx.coroutines.flow.Flow

class RecentlyViewedRepository(
    private val recentlyViewedReactiveStore: ReactiveStore<String, RecentlyViewedModel, Void>,
    private val drinkReactiveStore: ReactiveStore<String, DrinkPreviewModel, DrinkPreviewParam>
) {

    fun getRecentlyViewed(): Flow<List<DrinkPreviewModel>> {
        return drinkReactiveStore
            .getByParam(DrinkPreviewParam.SearchHistory)
    }

    fun storeAll(recentlyViewed: List<RecentlyViewedModel>) {
        recentlyViewedReactiveStore.storeAll(recentlyViewed)
    }


}