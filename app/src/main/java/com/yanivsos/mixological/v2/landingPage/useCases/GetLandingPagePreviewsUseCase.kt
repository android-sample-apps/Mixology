package com.yanivsos.mixological.v2.landingPage.useCases

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

class GetLandingPagePreviewsUseCase(
    private val latestArrivalsUseCase: GetLatestArrivalsUseCase,
    private val mostPopularsUseCase: GetMostPopularsUseCase,
    private val recentlyViewedUseCase: GetRecentlyViewedUseCase
) {
    val landingPagePreviews: Flow<LandingPageModel> =
        latestArrivals()
            .combine(mostPopulars()) { latestArrivals, mostPopulars ->
                LandingPageModel(
                    latestArrivals = latestArrivals,
                    mostPopulars = mostPopulars
                )
            }.combine(recentlyViewed()) { landingPageModel, recentlyViewed ->
                landingPageModel.copy(recentlyViewed = recentlyViewed)
            }.distinctUntilChanged()

    private fun latestArrivals() = latestArrivalsUseCase.latestArrivals
    private fun mostPopulars() = mostPopularsUseCase.mostPopulars
    private fun recentlyViewed() = recentlyViewedUseCase.recentlyViewed
}

data class LandingPageModel(
    val latestArrivals: List<DrinkPreviewModel> = emptyList(),
    val mostPopulars: List<DrinkPreviewModel> = emptyList(),
    val recentlyViewed: List<DrinkPreviewModel> = emptyList()
)
