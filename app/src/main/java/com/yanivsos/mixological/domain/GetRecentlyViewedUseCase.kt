package com.yanivsos.mixological.domain

import com.yanivsos.mixological.repo.repositories.RecentlyViewedRepository

class GetRecentlyViewedUseCase(
    combineWithFavoriteUseCase: CombineWithFavoriteUseCase,
    recentlyViewedRepository: RecentlyViewedRepository
) {
    val recentlyViewed =
        combineWithFavoriteUseCase
            .combine(recentlyViewedRepository.getRecentlyViewed())
}