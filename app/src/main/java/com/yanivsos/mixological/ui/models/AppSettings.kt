package com.yanivsos.mixological.ui.models

import com.yanivsos.mixological.v2.settings.AppDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


object AppSettings : KoinComponent {

    private val appDataStore: AppDataStore by inject()
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    // Dark Mode
    val darkModeEnabledFlow: Flow<Boolean> = appDataStore.darkModeEnabledFlow

    suspend fun setDarkModeEnabled(isEnabled: Boolean) =
        withContext(ioDispatcher) {
            appDataStore.setDarkModeEnabled(isEnabled)
        }

    //In app reviews
    val inAppReviewCounterFlow: Flow<Int> = appDataStore.inAppReviewCounterFlow


    suspend fun resetInAppReviewCounter() =
        withContext(ioDispatcher) {
            appDataStore.resetInAppReviewCounter()
        }

    suspend fun incrementInAppReviewCounter() =
        withContext(ioDispatcher) {
            appDataStore.incrementInAppReviewCounter()
        }
}
