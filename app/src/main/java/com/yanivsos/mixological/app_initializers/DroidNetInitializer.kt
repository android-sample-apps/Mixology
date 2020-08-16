package com.yanivsos.mixological.app_initializers

import android.content.Context
import androidx.startup.Initializer
import com.droidnet.DroidNet

class DroidNetInitializer : Initializer<DroidNet> {
    override fun create(context: Context): DroidNet {
        return DroidNet.init(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}