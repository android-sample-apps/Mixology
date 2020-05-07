package com.zemingo.drinksmenu.extensions

import android.app.Activity
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import com.zemingo.drinksmenu.ui.models.DrinkUiModel


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

fun Activity.setStatusBarColor(@ColorRes color: Int) {
//    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = ContextCompat.getColor(this, color)
}


fun Activity.shareDrink(drinkUiModel: DrinkUiModel) {
    ShareCompat.IntentBuilder
        .from(this)
        .setText(drinkUiModel.shareText)
        .setSubject(drinkUiModel.name)
        // most general text sharing MIME type
        .setType("text/plain")

        .setChooserTitle(drinkUiModel.name)
        .startChooser()
}