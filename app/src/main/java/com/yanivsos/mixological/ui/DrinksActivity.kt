package com.yanivsos.mixological.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.yanivsos.mixological.R
import com.yanivsos.mixological.v2.inAppReview.InAppReviewViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class DrinksActivity : AppCompatActivity(R.layout.activity_cocktail) {

    private val inAppReviewViewModel: InAppReviewViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeInAppReview()
    }

    private fun observeInAppReview() {
        Timber.d("observeInAppReview: ")
        inAppReviewViewModel
            .launchReviewFlow
            .flowWithLifecycle(lifecycle)
            .onEach { requestReviewFlow() }
            .launchIn(lifecycleScope)
    }

    private fun requestReviewFlow() {
        val reviewManager: ReviewManager by inject()
        Timber.d("requestReviewFlow: requesting review flow")
        reviewManager
            .requestReviewFlow()
            .addOnSuccessListener {
                Timber.d("requestReviewFlow: request successful: $it")
                launchReviewFlow(reviewManager, it)
            }
            .addOnFailureListener { e -> Timber.e(e, "requestReviewFlow: failed ") }
    }

    private fun launchReviewFlow(reviewManager: ReviewManager, reviewInfo: ReviewInfo) {
        Timber.d("launching in app review")
        reviewManager.launchReviewFlow(this, reviewInfo)
            .addOnCompleteListener {
                // The flow has finished. The API does not indicate whether the user
                // reviewed or not, or even whether the review dialog was shown. Thus, no
                // matter the result, we continue our app flow.
                Timber.d("in app review finished")
                inAppReviewViewModel.onLaunchReviewFlowCompleted()
            }
    }
}


