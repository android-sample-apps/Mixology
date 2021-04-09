package com.yanivsos.mixological.v2.inAppReview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import timber.log.Timber

class InAppReviewViewModel(
    private val launchReviewUseCase: LaunchReviewUseCase
) : ViewModel() {

    val launchReviewFlow = launchReviewUseCase
        .launchReviewFlow
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    fun onLaunchReviewFlowCompleted() {
        viewModelScope.launch {
            Timber.d("onReviewLaunchedCompleted")
            launchReviewUseCase.resetReviewCondition()
        }
    }

}
