package com.yanivsos.mixological.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class AnalyticsDispatcher(context: Context) {

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics


}