package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.repo.repositories.RecentlyViewedRepository

class GetRecentlyViewedUseCase(
    combineWithFavoriteUseCase: CombineWithFavoriteUseCase,
    recentlyViewedRepository: RecentlyViewedRepository
) {
    val recentlyViewed =
        combineWithFavoriteUseCase
            .combine(recentlyViewedRepository.getRecentlyViewed())
}