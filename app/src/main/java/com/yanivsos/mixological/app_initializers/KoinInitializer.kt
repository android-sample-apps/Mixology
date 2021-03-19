package com.yanivsos.mixological.app_initializers

import android.content.Context
import androidx.startup.Initializer
import com.yanivsos.mixological.di.KoinStarter

@Suppress("unused")
class KoinInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        KoinStarter().start(context)
        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
