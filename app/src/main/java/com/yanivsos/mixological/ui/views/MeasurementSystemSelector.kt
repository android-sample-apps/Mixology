package com.yanivsos.mixological.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.yanivsos.mixological.R
import com.yanivsos.mixological.conversions.*
import com.yanivsos.mixological.databinding.ViewMeasurementSystemConversionsSelectorBinding
import com.yanivsos.mixological.ui.models.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class MeasurementSystemSelector @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewMeasurementSystemConversionsSelectorBinding =
        ViewMeasurementSystemConversionsSelectorBinding.inflate(
            LayoutInflater.from(context), this
        )

    init {
        initViews()
    }

    private val _onMeasurementSystemFlow = MutableStateFlow(AppSettings.measurementSystem)
    val measurementSystemFlow: Flow<MeasurementPreference> =
        _onMeasurementSystemFlow
            .map { MeasurementSystemParser.parse(it) }

    private fun initViews() {
        initSystemMode()
        setOnSystemChangedListener()
    }

    private fun initSystemMode() {
        when (AppSettings.measurementSystem) {
            MEASUREMENT_SYSTEM_ORIGINAL -> binding.originalRb
            MEASUREMENT_SYSTEM_METRIC -> binding.metricRb
            MEASUREMENT_SYSTEM_IMPERIAL -> binding.imperialRb
            else -> binding.originalRb
        }.run {
            isChecked = true
        }
    }

    private fun setOnSystemChangedListener() {
        binding.conversionsRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.original_rb -> onOriginalChecked()
                R.id.metric_rb -> onMetricChecked()
                R.id.imperial_rb -> onImperialChecked()
            }
        }
    }

    private fun onMetricChecked() {
        Timber.d("onMetricChecked")
        _onMeasurementSystemFlow.value = MEASUREMENT_SYSTEM_METRIC
    }

    private fun onImperialChecked() {
        Timber.d("onImperialChecked")
        _onMeasurementSystemFlow.value = MEASUREMENT_SYSTEM_IMPERIAL
    }

    private fun onOriginalChecked() {
        Timber.d("onOriginalChecked")
        _onMeasurementSystemFlow.value = MEASUREMENT_SYSTEM_ORIGINAL
    }
}
