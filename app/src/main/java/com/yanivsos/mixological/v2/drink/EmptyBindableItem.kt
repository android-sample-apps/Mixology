package com.yanivsos.mixological.v2.drink

import androidx.viewbinding.ViewBinding
import com.xwray.groupie.viewbinding.BindableItem

abstract class EmptyBindableItem<T : ViewBinding> : BindableItem<T>() {
    override fun bind(viewBinding: T, position: Int) {
        //nothing to do
    }
}
