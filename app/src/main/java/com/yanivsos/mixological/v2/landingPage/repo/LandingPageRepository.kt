package com.yanivsos.mixological.v2.landingPage.repo

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.domain.models.LatestArrivalsModel
import com.yanivsos.mixological.domain.models.MostPopularModel
import com.yanivsos.mixological.domain.models.RecentlyViewedModel
import com.yanivsos.mixological.repo.DrinkService
import com.yanivsos.mixological.v2.drink.mappers.toModel
import com.yanivsos.mixological.v2.landingPage.dao.LandingPageDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LandingPageRepository(
    private val drinkService: DrinkService,
    private val landingPageDao: LandingPageDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    //latest arrivals
    fun getLatestArrivals(): Flow<List<DrinkPreviewModel>> = landingPageDao.getLatestArrivals()

    //Most populars
    fun getMostPopulars(): Flow<List<DrinkPreviewModel>> = landingPageDao.getMostPopulars()

    //recently viewed
    fun getRecentlyViewed(): Flow<List<DrinkPreviewModel>> = landingPageDao.getRecentlyViewed()

    suspend fun replaceMostPopulars(mostPopulars: List<MostPopularModel>) {
        return withContext(ioDispatcher) {
            landingPageDao.replaceMostPopular(mostPopulars)
        }
    }

    suspend fun replaceLatestArrivals(latestArrivals: List<LatestArrivalsModel>) {
        return withContext(ioDispatcher) {
            landingPageDao.replaceLatestArrivals(latestArrivals)
        }
    }

    suspend fun fetchMostPopular(): List<DrinkPreviewModel> {
        return withContext(ioDispatcher) {
            val response = drinkService.getMostPopular()
            withContext(defaultDispatcher) {
                response.toModel()
            }
        }
    }

    suspend fun fetchLatestArrivals(): List<DrinkPreviewModel> {
        return withContext(ioDispatcher) {
            val response = drinkService.getLatestArrivals()
            withContext(defaultDispatcher) {
                response.toModel()
            }
        }
    }

    suspend fun addToRecentlyViewed(recentlyViewedModel: RecentlyViewedModel) {
        withContext(ioDispatcher) {
            landingPageDao.insertRecentlyViewed(recentlyViewedModel)
        }
    }
}
