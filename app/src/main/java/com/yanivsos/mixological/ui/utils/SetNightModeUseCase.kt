package com.yanivsos.mixological.ui.utils

import androidx.appcompat.app.AppCompatDelegate

class SetNightModeUseCase {

    fun setNightMode(isNightMode: Boolean) {
        val mode =
            if (isNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}