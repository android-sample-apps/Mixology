package com.yanivsos.mixological.in_app_review

import com.yanivsos.mixological.ui.models.AppSettings

class InAppReviewRepository {

    companion object {
        private const val FAVORITE_THRESHOLD = 3
    }

    fun shouldShowInAppReview(): Boolean {
        return AppSettings.inAppReviewCounter >= FAVORITE_THRESHOLD
    }

    fun resetShowInAppReviewCondition() {
        AppSettings.inAppReviewCounter = 0
    }

    fun onFavoriteAdded() {
        AppSettings.inAppReviewCounter += 1
    }
}