package com.zemingo.drinksmenu.extensions

import android.widget.ImageView
import androidx.annotation.DrawableRes

fun ImageView.fromLink(
    link: String?,
    @DrawableRes placeHolderRes: Int? = null
) {
    context.toGlideBuilder(link)
        .apply {
            placeHolderRes?.let { placeholder(it) }
        }
        .into(this)
}