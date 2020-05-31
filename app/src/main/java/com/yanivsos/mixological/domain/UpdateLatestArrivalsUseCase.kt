package com.yanivsos.mixological.domain

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.domain.models.LatestArrivalsModel
import com.yanivsos.mixological.repo.repositories.DrinkPreviewRepository
import com.yanivsos.mixological.repo.repositories.LatestArrivalsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class UpdateLatestArrivalsUseCase(
    private val latestArrivalsRepository: LatestArrivalsRepository,
    private val drinkPreviewRepository: DrinkPreviewRepository
) {

    fun update() {
        GlobalScope.launch(Dispatchers.IO) {
            replaceLatestArrivals()
        }
    }

    private suspend fun replaceLatestArrivals() {
        Timber.d("replacing latest arrivals")
        try {
            val latestArrivalsPreviews = latestArrivalsRepository.fetchLatestArrivals()
            val latestArrivals = toLatestArrivals(latestArrivalsPreviews)
            latestArrivalsRepository.removeAll()
            latestArrivalsRepository.storeAll(latestArrivals)
            drinkPreviewRepository.storeAll(latestArrivalsPreviews)
        } catch (e: Exception) {
            Timber.e(e, "Failed replacing latest arrivals")
        }
    }

    private fun toLatestArrivals(previews: List<DrinkPreviewModel>): List<LatestArrivalsModel> {
        return previews.map {
            LatestArrivalsModel(drinkId = it.id)
        }
    }
}