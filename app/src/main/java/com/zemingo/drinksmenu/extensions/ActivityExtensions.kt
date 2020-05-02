package com.zemingo.drinksmenu.extensions

import android.app.Activity
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