package com.yanivsos.mixological.domain

import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.domain.models.LatestArrivalsModel
import com.yanivsos.mixological.domain.models.MostPopularModel
import com.yanivsos.mixological.repo.repositories.DrinkPreviewRepository
import com.yanivsos.mixological.repo.repositories.MostPopularRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class UpdateMostPopularUseCase(
    private val mostPopularRepository: MostPopularRepository,
    private val drinkPreviewRepository: DrinkPreviewRepository
) {

    fun update() {
        GlobalScope.launch(Dispatchers.IO) {
            replaceMostPopular()
        }
    }

    private suspend fun replaceMostPopular() {
        Timber.d("replacing most popular")
        try {
            val mostPopularPreviews = mostPopularRepository.fetchMostPopulars()
            val mostPopular = toMostPopular(mostPopularPreviews)
            mostPopularRepository.removeAll()
            mostPopularRepository.storeAll(mostPopular)
            drinkPreviewRepository.storeAll(mostPopularPreviews)
        } catch (e: Exception) {
            Timber.e(e, "Failed replacing most popular")
        }
    }

    private fun toMostPopular(previews: List<DrinkPreviewModel>): List<MostPopularModel> {
        return previews.map {
            MostPopularModel(drinkId = it.id)
        }
    }
}