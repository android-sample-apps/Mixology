package com.yanivsos.mixological.app_initializers

import android.content.Context
import androidx.startup.Initializer
import timber.log.Timber

@Suppress("unused")
class TimberInitializer: Initializer<Unit> {
    override fun create(context: Context) {
        Timber.plant(Timber.DebugTree())
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
