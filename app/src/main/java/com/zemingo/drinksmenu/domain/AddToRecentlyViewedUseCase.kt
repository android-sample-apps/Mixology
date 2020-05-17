package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.RecentlyViewedModel
import com.zemingo.drinksmenu.repo.repositories.RecentlyViewedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class AddToRecentlyViewedUseCase(
    private val repository: RecentlyViewedRepository
) {
    fun add(id: String) {
        Timber.d("drinkPreview[$id], added as previously viewed")
        GlobalScope.launch(Dispatchers.IO) {
            repository.storeAll(
                listOf(
                    RecentlyViewedModel(
                        drinkId = id,
                        lastViewedTime = Date().time
                    )
                )
            )
        }
    }
}