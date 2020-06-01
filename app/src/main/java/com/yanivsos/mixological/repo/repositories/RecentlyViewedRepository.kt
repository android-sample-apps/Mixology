package com.yanivsos.mixological.repo.repositories

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.domain.models.RecentlyViewedModel
import com.yanivsos.mixological.repo.reactiveStore.DrinkPreviewParam
import com.yanivsos.mixological.repo.reactiveStore.NonRemovableReactiveStore
import com.yanivsos.mixological.repo.reactiveStore.RecentlyViewedParams
import kotlinx.coroutines.flow.Flow

class RecentlyViewedRepository(
    private val recentlyViewedReactiveStore: NonRemovableReactiveStore<RecentlyViewedModel, RecentlyViewedParams>,
    private val drinkReactiveStore: NonRemovableReactiveStore<DrinkPreviewModel, DrinkPreviewParam>
) {

    fun getRecentlyViewed(): Flow<List<DrinkPreviewModel>> {
        return drinkReactiveStore
            .get(DrinkPreviewParam.SearchHistory)
    }

    fun storeAll(recentlyViewed: List<RecentlyViewedModel>) {
        recentlyViewedReactiveStore.storeAll(recentlyViewed)
    }
}