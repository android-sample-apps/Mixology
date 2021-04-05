package com.yanivsos.mixological.v2.landingPage.useCases

import com.yanivsos.mixological.database.DrinkPreviewModel
import com.yanivsos.mixological.database.LatestArrivalsModel
import com.yanivsos.mixological.database.MostPopularModel
import com.yanivsos.mixological.v2.landingPage.repo.LandingPageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class RefreshLandingPagePreviewsUseCase(
    private val landingPageRepository: LandingPageRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend fun refresh() {
        refreshLatestArrivals()
        refreshMostPopulars()
    }

    private suspend fun refreshLatestArrivals() {
        Timber.d("refreshing latest arrivals ...")
        runCatching { landingPageRepository.fetchLatestArrivals() }
            .onSuccess {
                landingPageRepository.replaceLatestArrivals(mapToLatestArrivals(it))
                Timber.d("finished refreshing latest arrivals")
            }
            .onFailure { Timber.e("Failed to refresh latest arrivals") }
    }

    private suspend fun refreshMostPopulars() {
        Timber.d("refreshing most populars ...")
        runCatching { landingPageRepository.fetchMostPopular() }
            .onSuccess {
                landingPageRepository.replaceMostPopulars(mapToMostPopulars(it))
                Timber.d("finished refreshing most populars")
            }
            .onFailure { Timber.e("Failed to refresh most populars") }
    }

    private suspend fun mapToLatestArrivals(latestArrivals: List<DrinkPreviewModel>): List<LatestArrivalsModel> {
        return withContext(defaultDispatcher) {
            latestArrivals.map { LatestArrivalsModel(it.id) }
        }
    }

    private suspend fun mapToMostPopulars(mostPopulars: List<DrinkPreviewModel>): List<MostPopularModel> {
        return withContext(defaultDispatcher) {
            mostPopulars.map { MostPopularModel(it.id) }
        }
    }
}
