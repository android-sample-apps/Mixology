package com.yanivsos.mixological.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.yanivsos.mixological.R
import com.yanivsos.mixological.conversions.MeasurementPreference
import com.yanivsos.mixological.databinding.FragmentSettingsBinding
import com.yanivsos.mixological.ui.view_model.SettingsViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding
            .settingsDarkMode.run {
                isChecked = settingsViewModel.darkModeEnabled
                checkedChangedChannel
                    .onEach { onDarkModeEnabled(it) }
                    .launchIn(viewLifecycleOwner.lifecycleScope)
            }

        binding.settingsMeasurementSelector
            .measurementSystemFlow
            .onEach { onMeasurementPreferenceChanged(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onDarkModeEnabled(darkModeEnabled: Boolean) {
        Timber.d("onDarkModeEnabled: $darkModeEnabled")
        settingsViewModel.darkModeEnabled = (darkModeEnabled)
    }

    private fun onMeasurementPreferenceChanged(measurementPreference: MeasurementPreference) {
        Timber.d("measurementPreference: $measurementPreference")
        settingsViewModel.changeMeasurementSystemPreference(measurementPreference)
    }
}
