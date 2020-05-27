package com.zemingo.drinksmenu.extensions

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions

@ColorInt
fun Context.compatColor(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}

fun Context.toGlideBuilder(
    link: String?
): RequestBuilder<Bitmap> {
    return Glide
        .with(this)
        .asBitmap()
        .load(link)
        .transition(BitmapTransitionOptions.withCrossFade(250))
}