package com.yanivsos.mixological.in_app_review

class ShouldShowInAppReviewUseCase(
    private val inAppReviewRepository: InAppReviewRepository
) {

    fun shouldShowInAppReview(): Boolean {
        return inAppReviewRepository.shouldShowInAppReview()
    }

    fun resetShouldShowInAppReviewCondition() {
        inAppReviewRepository.resetShowInAppReviewCondition()
    }
}