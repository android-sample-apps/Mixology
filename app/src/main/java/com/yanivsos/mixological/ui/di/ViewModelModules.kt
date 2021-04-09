package com.yanivsos.mixological.ui.di

import com.yanivsos.mixological.ui.view_model.ConnectivityViewModel
import com.yanivsos.mixological.ui.view_model.SettingsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        ConnectivityViewModel()
    }

    viewModel {
        SettingsViewModel()
    }
}
