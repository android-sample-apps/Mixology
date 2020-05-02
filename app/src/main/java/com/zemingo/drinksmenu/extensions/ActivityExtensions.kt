package com.zemingo.drinksmenu.extensions

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowManager


fun Activity.translucentStatusBar() {
    window.setFlags(
        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
    )
}

fun Activity.clearTranslucentStatusBar() {
    window.clearFlags(
        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
    )
}


fun clearLightStatusBar(view: View) {
    var flags: Int = view.systemUiVisibility
    flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    view.systemUiVisibility = flags
}