package com.yanivsos.mixological.extensions

import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.annotation.Dimension

@Dimension(unit = Dimension.PX)
fun Number.dpToPx(
): Float {
    val metrics: DisplayMetrics = Resources.getSystem().displayMetrics
    return toFloat() * metrics.density
}
