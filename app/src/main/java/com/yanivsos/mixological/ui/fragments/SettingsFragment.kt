package com.yanivsos.mixological.ui.fragments

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.yanivsos.mixological.R
import com.yanivsos.mixological.ui.view_model.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            settings_dark_mode.apply {
                isChecked = settingsViewModel.darkModeEnabled
                checkedChangedChannel
                    .collect { darkModeEnabled ->
                        onDarkModeEnabled(darkModeEnabled)
                    }
            }
        }
    }

    private fun onDarkModeEnabled(darkModeEnabled: Boolean) {
        Timber.d("onDarkModeEnabled: $darkModeEnabled")
        settingsViewModel.darkModeEnabled = (darkModeEnabled)
    }
}