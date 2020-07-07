package com.yanivsos.mixological.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yanivsos.mixological.R
import com.yanivsos.mixological.analytics.AnalyticsDispatcher

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun onResume() {
        super.onResume()
        AnalyticsDispatcher.setCurrentScreen(this)
    }

    private fun themeContext(): ContextThemeWrapper {
        //https://github.com/material-components/material-components-android/issues/99#issuecomment-390380852
        return ContextThemeWrapper(requireActivity(), R.style.AppTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //https://github.com/material-components/material-components-android/issues/99#issuecomment-390380852
        return createView(
            inflater = inflater.cloneInContext(themeContext()),
            container = container,
            savedInstanceState = savedInstanceState
        )
    }

    abstract fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
}