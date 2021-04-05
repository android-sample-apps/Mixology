package com.yanivsos.mixological.v2.drink.useCases

import com.yanivsos.mixological.database.RecentlyViewedModel
import com.yanivsos.mixological.v2.landingPage.repo.LandingPageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*

class AddToRecentlyViewedUseCase(
    private val landingPageRepository: LandingPageRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend fun addToRecentlyViewed(id: String) {
        Timber.d("addToRecentlyViewed: adding id[$id] ...")
        landingPageRepository.addToRecentlyViewed(createRecentlyViewedModel(id))
        Timber.d("addToRecentlyViewed: added id[$id]")
    }

    private suspend fun createRecentlyViewedModel(id: String): RecentlyViewedModel {
        return withContext(defaultDispatcher) {
            RecentlyViewedModel(
                drinkId = id,
                lastViewedTime = Date().time
            )
        }
    }
}
