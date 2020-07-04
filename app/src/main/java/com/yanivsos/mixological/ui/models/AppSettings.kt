package com.yanivsos.mixological.ui.models

import com.chibatching.kotpref.KotprefModel

object AppSettings: KotprefModel() {
    var darkModeEnabled by booleanPref(default = false)
}