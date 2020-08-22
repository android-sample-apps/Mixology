package com.yanivsos.mixological.in_app_review

import android.app.Activity
import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewManager
import timber.log.Timber

class RequestInAppReviewUseCase(
    private val reviewManager: ReviewManager
) {

    suspend fun launchReview(activity: Activity) {
        try {
            reviewManager.run {
                val reviewInfo = requestReview()
                launchReview(activity, reviewInfo)
            }
        } catch (e: Exception) {
            Timber.e(e, "unable to launch review")
        }
    }
}