package com.zemingo.drinksmenu.extensions

import android.widget.ImageView
import coil.api.load
import coil.transform.CircleCropTransformation
import coil.transform.Transformation

fun ImageView.fromLink(link: String?) {
    this.load(link) {
        crossfade(250)
    }
}
fun ImageView.fromLink(link: String?, vararg transformations: Transformation) {
    this.load(link) {
        crossfade(250)
        transformations(transformations.toList())
    }
}