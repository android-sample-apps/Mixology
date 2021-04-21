package com.yanivsos.mixological.v2.settings

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val settingsDi = module {
    single { AppDataStore(androidContext()) }
}
