package com.zemingo.drinksmenu.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zemingo.drinksmenu.domain.GetLatestArrivalsUseCase
import com.zemingo.drinksmenu.domain.GetMostPopularUseCase
import com.zemingo.drinksmenu.domain.GetRecentlyViewedUseCase
import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.ui.models.DrinkPreviewUiModel
import com.zemingo.drinksmenu.ui.models.LandingPageUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.function.Function

class LandingPageViewModel(
    private val mostPopularUseCase: GetMostPopularUseCase,
    private val latestArrivalsUseCase: GetLatestArrivalsUseCase,
    private val recentSearchesUseCase: GetRecentlyViewedUseCase,
    private val mapper: Function<List<DrinkPreviewModel>, List<DrinkPreviewUiModel>>
) : ViewModel() {

    val landingPageLiveData: LiveData<LandingPageUiModel> =
        landingPage().asLiveData()

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