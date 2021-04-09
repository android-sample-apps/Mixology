package com.yanivsos.mixological.v2.inAppReview

import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import org.koin.dsl.module
import com.yanivsos.mixological.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel

val inAppReviewModule = module {

    single {
        if (BuildConfig.DEBUG) FakeReviewManager(androidContext()) else ReviewManagerFactory.create(
            androidContext()
        )
    }

    single {
        InAppReviewRepository()
    }

    factory {
        LaunchReviewUseCase(
            inAppReviewRepository = get()
        )
    }

    viewModel {
        InAppReviewViewModel(
            launchReviewUseCase = get()
        )
    }
}
