package com.yanivsos.mixological.ui.utils

import android.os.SystemClock
import android.view.View

fun View.setOnSingleClickListener(listener: (View) -> Unit) {
    setOnClickListener(OnSingleClickListener(listener))
}

class OnSingleClickListener(listener: (View) -> Unit) : View.OnClickListener {

    private companion object {
        private const val DELAY_MILLIS = 200L
        private var previousClickTimeMillis = 0L
    }

    private val onClickListener: View.OnClickListener = View.OnClickListener(listener)

    override fun onClick(v: View) {
        val elapsedRealtime = SystemClock.elapsedRealtime()
        if (elapsedRealtime >= previousClickTimeMillis + DELAY_MILLIS) {
            previousClickTimeMillis = elapsedRealtime
            onClickListener.onClick(v)
        }
    }
}
