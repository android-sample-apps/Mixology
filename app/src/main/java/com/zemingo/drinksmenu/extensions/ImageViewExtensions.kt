package com.zemingo.drinksmenu.extensions

import android.widget.ImageView
import coil.api.load

fun ImageView.fromLink(link: String?) {
    this.load(link) {
        crossfade(250)
    }
}