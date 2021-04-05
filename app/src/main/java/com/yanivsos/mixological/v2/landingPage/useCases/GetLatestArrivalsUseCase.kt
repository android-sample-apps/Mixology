package com.yanivsos.mixological.v2.landingPage.useCases

import com.yanivsos.mixological.database.DrinkPreviewModel
import com.yanivsos.mixological.v2.landingPage.repo.LandingPageRepository
import kotlinx.coroutines.flow.Flow

class GetLatestArrivalsUseCase(
    landingPageRepository: LandingPageRepository
) {

    val latestArrivals: Flow<List<DrinkPreviewModel>> = landingPageRepository.getLatestArrivals()
}
