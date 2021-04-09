package com.yanivsos.mixological.v2.inAppReview

import com.yanivsos.mixological.ui.models.AppSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class InAppReviewRepository(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    val inAppReviewCounterFlow: Flow<Int> = AppSettings.inAppReviewCounterFlow

    suspend fun resetLaunchReviewCondition() = withContext(ioDispatcher) {
        AppSettings.resetInAppReviewCounter()
    }

    suspend fun incrementInAppReviewCounter() = withContext(ioDispatcher) {
        AppSettings.incrementInAppReviewCounter()
    }
}
