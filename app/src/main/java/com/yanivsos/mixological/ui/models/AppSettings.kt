package com.yanivsos.mixological.ui.models

import com.chibatching.kotpref.KotprefModel
import com.yanivsos.mixological.conversions.MeasurementSystemParser

object AppSettings: KotprefModel() {
    var darkModeEnabled by booleanPref(default = false)
    var inAppReviewCounter by intPref(default = 0)
    var measurementSystem by intPref(default = MeasurementSystemParser.ORIGINAL)
}
