package com.yanivsos.mixological.extensions

import android.app.Activity
import androidx.core.app.ShareCompat
import com.yanivsos.mixological.ui.models.DrinkUiModel


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