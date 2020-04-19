package com.zemingo.drinksmenu.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.zemingo.drinksmenu.models.ImageModel

fun ImageModel.into(imageView: ImageView) {
    Glide.with(imageView).load(link).into(imageView)
}
