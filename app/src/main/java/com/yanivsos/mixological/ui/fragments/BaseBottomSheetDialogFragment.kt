package com.yanivsos.mixological.ui.fragments

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yanivsos.mixological.analytics.AnalyticsDispatcher

abstract class BaseBottomSheetDialogFragment: BottomSheetDialogFragment() {

    override fun onResume() {
        super.onResume()
        AnalyticsDispatcher.setCurrentScreen(this)
    }
}