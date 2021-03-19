package com.yanivsos.mixological.v2.landingPage.useCases

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.v2.landingPage.repo.LandingPageRepository
import kotlinx.coroutines.flow.Flow

class GetMostPopularsUseCase(
    landingPageRepository: LandingPageRepository
) {

    val mostPopulars: Flow<List<DrinkPreviewModel>> = landingPageRepository.getMostPopulars()
}
