package com.yanivsos.mixological.in_app_review

import android.app.Activity
import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewManager

class RequestInAppReviewUseCase(
    private val reviewManager: ReviewManager
) {

    suspend fun launchReview(activity: Activity) {
        reviewManager.run {
            val reviewInfo = requestReview()
            launchReview(activity, reviewInfo)
        }
    }
}