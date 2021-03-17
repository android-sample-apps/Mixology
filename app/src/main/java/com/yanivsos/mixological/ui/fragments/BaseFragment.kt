package com.yanivsos.mixological.ui.fragments

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.yanivsos.mixological.analytics.AnalyticsDispatcher

abstract class BaseFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    override fun onResume() {
        super.onResume()
        AnalyticsDispatcher.setCurrentScreen(this)
    }
}

fun Fragment.viewLifecycleScope() = viewLifecycleOwner.lifecycleScope
