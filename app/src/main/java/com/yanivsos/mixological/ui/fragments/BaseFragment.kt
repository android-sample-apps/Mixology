package com.yanivsos.mixological.ui.fragments

import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.yanivsos.mixological.analytics.AnalyticsDispatcher
import kotlinx.coroutines.flow.Flow

abstract class BaseFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    override fun onResume() {
        super.onResume()
        AnalyticsDispatcher.setCurrentScreen(this)
    }

    protected fun <T> Flow<T>.withLifecycle() = flowWithLifecycle(viewLifecycleOwner.lifecycle)
}

fun Fragment.viewLifecycleScope() = viewLifecycleOwner.lifecycleScope


