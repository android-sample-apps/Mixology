package com.yanivsos.mixological.v2.inAppReview

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

private const val FAVORITE_THRESHOLD = 3

class LaunchReviewUseCase(
    private val inAppReviewRepository: InAppReviewRepository
) {

    val launchReviewFlow: Flow<Unit> =
        inAppReviewRepository
            .inAppReviewCounterFlow
            .onEach { Timber.d("inAppReview counter: $it") }
            .filter { counter -> counter >= FAVORITE_THRESHOLD }
            .map { Unit }

    suspend fun resetReviewCondition() {
        inAppReviewRepository.resetLaunchReviewCondition()
    }
}
