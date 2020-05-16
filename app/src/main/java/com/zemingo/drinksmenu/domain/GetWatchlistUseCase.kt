package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.DrinkPreviewModel
import com.zemingo.drinksmenu.repo.repositories.DrinkPreviewRepository
import com.zemingo.drinksmenu.repo.repositories.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map

class GetWatchlistUseCase(
    watchlistRepository: WatchlistRepository,
    drinkPreviewRepository: DrinkPreviewRepository
) {

    val watchList: Flow<List<DrinkPreviewModel>> =
        watchlistRepository
            .getWatchlist()
            .map { watchlist -> watchlist.map { it.id } }
            .flatMapMerge { drinkPreviewRepository.getByIds(it) }

}