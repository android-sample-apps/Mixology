package com.yanivsos.mixological.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.yanivsos.mixological.R
import kotlinx.android.synthetic.main.view_settings_switch.view.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class SettingsSwitch @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val _checkedChangedChannel = ConflatedBroadcastChannel<Boolean>()
    val checkedChangedChannel = _checkedChangedChannel.asFlow().distinctUntilChanged()

    var isChecked: Boolean = false
        set(value) {
            settings_switch.isChecked = value
            field = value
        }

    init {
        View.inflate(context, R.layout.view_settings_switch, this)
        initSwitch()
    }

    private fun initSwitch() {
        setOnClickListener {
            settings_switch.toggle()
        }

        settings_switch.setOnCheckedChangeListener { _, isChecked ->
            _checkedChangedChannel.offer(isChecked)
        }
    }
}