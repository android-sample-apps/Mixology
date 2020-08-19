package com.yanivsos.mixological.in_app_review

import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import com.yanivsos.mixological.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val inAppReviewModule = module {

    factory {
        if (BuildConfig.DEBUG) {
            FakeReviewManager(androidContext())
        } else {
            ReviewManagerFactory.create(androidContext())
        }
    }

    factory {
        RequestInAppReviewUseCase(get())
    }
}