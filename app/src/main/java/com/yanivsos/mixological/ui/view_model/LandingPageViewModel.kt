package com.yanivsos.mixological.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.yanivsos.mixological.domain.GetLatestArrivalsUseCase
import com.yanivsos.mixological.domain.GetMostPopularUseCase
import com.yanivsos.mixological.domain.GetRecentlyViewedUseCase
import com.yanivsos.mixological.domain.UpdateLatestArrivalsUseCase
import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.ui.models.DrinkPreviewUiModel
import com.yanivsos.mixological.ui.models.LandingPageUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.function.Function

class LandingPageViewModel(
    private val mostPopularUseCase: GetMostPopularUseCase,
    private val latestArrivalsUseCase: GetLatestArrivalsUseCase,
    private val recentSearchesUseCase: GetRecentlyViewedUseCase,
    updateLatestArrivalsUseCase: UpdateLatestArrivalsUseCase,
    private val mapper: Function<List<DrinkPreviewModel>, List<DrinkPreviewUiModel>>
) : ViewModel() {

    val landingPageLiveData: LiveData<LandingPageUiModel> =
        landingPage().asLiveData()

    init {
        updateLatestArrivalsUseCase.update()
    }

    private fun landingPage(): Flow<LandingPageUiModel> {
        return latestArrivals().combine(mostPopular()) { latestArrivals, popular ->
            LandingPageUiModel(
                mostPopular = popular,
                latestArrivals = latestArrivals,
                recentSearches = emptyList()
            )
        }.combine(recentSearches()) { landingPage, recentSearches ->
            landingPage.copy(recentSearches = recentSearches)
        }
    }

    private fun latestArrivals(): Flow<List<DrinkPreviewUiModel>> {
        return latestArrivalsUseCase
            .latestArrivals
            .map { mapper.apply(it) }
    }

    private fun mostPopular(): Flow<List<DrinkPreviewUiModel>> {
        return mostPopularUseCase
            .mostPopular
            .map { mapper.apply(it) }
    }

    private fun recentSearches(): Flow<List<DrinkPreviewUiModel>> {
        return recentSearchesUseCase
            .recentlyViewed
            .map { mapper.apply(it) }
    }

}