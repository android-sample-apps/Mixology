package com.yanivsos.mixological.v2.startup

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yanivsos.mixological.v2.landingPage.useCases.FetchPreviewsByLetterUseCase
import org.koin.java.KoinJavaComponent.inject

class FetchPreviewsWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    companion object {
        const val WORKER_NAME = "FetchPreviewsWorker"
    }

    private val fetchPreviewsUseCase: FetchPreviewsByLetterUseCase by inject(
        FetchPreviewsByLetterUseCase::class.java
    )

    override suspend fun doWork(): Result {
        fetchPreviewsUseCase.fetchPreviewsByLetters()
        return Result.success()
    }
}
