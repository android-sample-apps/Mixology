package com.yanivsos.mixological.v2.landingPage

import com.yanivsos.mixological.ui.models.LandingPageUiModel
import com.yanivsos.mixological.v2.drink.mappers.toUiModel
import com.yanivsos.mixological.v2.landingPage.useCases.LandingPageModel

fun LandingPageModel.toUiModel(): LandingPageUiModel {
    return LandingPageUiModel(
        mostPopular = mostPopulars.map { it.toUiModel() },
        latestArrivals = latestArrivals.map { it.toUiModel() },
        recentlyViewed = recentlyViewed.map { it.toUiModel() }
    )
}
