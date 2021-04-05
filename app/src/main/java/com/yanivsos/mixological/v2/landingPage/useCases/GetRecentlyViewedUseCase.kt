package com.yanivsos.mixological.v2.landingPage.useCases

import com.yanivsos.mixological.database.DrinkPreviewModel
import com.yanivsos.mixological.v2.landingPage.repo.LandingPageRepository
import kotlinx.coroutines.flow.Flow

class GetRecentlyViewedUseCase(
    landingPageRepository: LandingPageRepository
) {

    val recentlyViewed: Flow<List<DrinkPreviewModel>> = landingPageRepository.getRecentlyViewed()
}
