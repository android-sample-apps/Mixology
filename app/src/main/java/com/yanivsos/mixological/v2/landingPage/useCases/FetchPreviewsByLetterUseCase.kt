package com.yanivsos.mixological.v2.landingPage.useCases

import com.yanivsos.mixological.v2.drink.repo.DrinkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class FetchPreviewsByLetterUseCase(
    private val drinkRepository: DrinkRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend fun fetchPreviewsByLetters() {
        withContext(defaultDispatcher) {
            forEachLetter { letter -> fetchByLetter(letter) }
        }
    }

    private suspend fun fetchByLetter(letter: Char) {
        runCatching {
            Timber.d("fetching by letter $letter")
            drinkRepository.fetchPreviewsByLetter(letter)
        }
            .onSuccess { drinkRepository.storePreviews(it) }
            .onFailure { Timber.w(it, "failed fetching by letter $letter") }
    }

    private inline fun forEachLetter(onLetter: (Char) -> Unit) {
        var char = 'A'
        while (char <= 'Z') {
            onLetter(char)
            ++char
        }
    }
}
