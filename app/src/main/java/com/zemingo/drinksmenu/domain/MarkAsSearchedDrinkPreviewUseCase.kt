package com.zemingo.drinksmenu.domain

import com.zemingo.drinksmenu.domain.models.PreviousSearchModel
import com.zemingo.drinksmenu.repo.repositories.SearchDrinkPreviewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class MarkAsSearchedDrinkPreviewUseCase(
    private val repository: SearchDrinkPreviewRepository
) {
    fun markAsSearched(id: String) {
        Timber.d("drinkPreview[$id], marked as searched")
        GlobalScope.launch(Dispatchers.IO) {
            repository.storeAll(
                listOf(
                    PreviousSearchModel(
                        drinkId = id,
                        lastViewedTime = Date().time
                    )
                )
            )
        }
    }
}