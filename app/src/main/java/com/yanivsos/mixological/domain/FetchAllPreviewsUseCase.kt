package com.yanivsos.mixological.domain

import com.yanivsos.mixological.repo.repositories.DrinkPreviewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class FetchAllPreviewsUseCase(
    private val previewRepository: DrinkPreviewRepository
) {

    fun fetchAll() {
        GlobalScope.launch(Dispatchers.IO) {
            var c = 'A'
            while (c <= 'Z') {
                Timber.d("fetching previews with letter[$c]")
                previewRepository.fetchByLetter(c.toString())
                Timber.d("finished fetching")
                ++c
            }
        }
    }
}