package com.zemingo.drinksmenu.domain

import timber.log.Timber

class MarkAsSearchedDrinkPreviewUseCase {

    fun markAsSearched(id: String) {
        Timber.d("drinkPreview[$id], marked as searched")
    }
}