package com.yanivsos.mixological.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.yanivsos.mixological.R
import com.yanivsos.mixological.databinding.ViewSettingsSwitchBinding
import com.yanivsos.mixological.extensions.getStringFromResourceId
import kotlinx.coroutines.flow.*

class SettingsSwitch @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val _checkedChangedChannel = MutableStateFlow<Boolean?>(null)
    val checkedChangedChannel: Flow<Boolean> = _checkedChangedChannel.filterNotNull()


    var isChecked: Boolean = false
        set(value) {
            binding.settingsSwitch.isChecked = value
            field = value
        }

    private val binding = ViewSettingsSwitchBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    init {
        initAttributes(attrs)
        initSwitch()
    }

    private fun initAttributes(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SettingsSwitch, 0, 0
        ).apply {
            try {
                binding.settingsTitleTv.text =
                    getStringFromResourceId(R.styleable.SettingsSwitch_ss_title)
                binding.settingsSubtitleTv.text =
                    getStringFromResourceId(R.styleable.SettingsSwitch_ss_description)
            } finally {
                recycle()
            }
        }
    }

    private fun initSwitch() {
        setOnClickListener {
            binding.settingsSwitch.toggle()
        }

        binding.settingsSwitch.setOnCheckedChangeListener { _, isChecked ->
            _checkedChangedChannel.value = isChecked
        }
    }
}
