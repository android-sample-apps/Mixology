package com.zemingo.drinksmenu.extensions

import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil.api.load
import coil.transform.Transformation

fun ImageView.fromLink(link: String?, @DrawableRes placeHolder: Int? = null) {
    this.load(link) {
        crossfade(250)
        placeHolder?.let {
            error(it)
            placeholder(it) }
    }
}

fun ImageView.fromLink(link: String?, vararg transformations: Transformation) {
    this.load(link) {
        crossfade(250)
        transformations(transformations.toList())
    }
}