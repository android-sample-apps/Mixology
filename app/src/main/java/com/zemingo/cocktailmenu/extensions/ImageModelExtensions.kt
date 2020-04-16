package com.zemingo.cocktailmenu.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.zemingo.cocktailmenu.models.ImageModel

fun ImageModel.into(imageView: ImageView) {
    Glide.with(imageView).load(link).into(imageView)
}
