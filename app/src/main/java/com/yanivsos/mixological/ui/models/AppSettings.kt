package com.yanivsos.mixological.ui.models

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.chibatching.kotpref.KotprefModel
import com.yanivsos.mixological.conversions.MEASUREMENT_SYSTEM_ORIGINAL
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "appSettings")

private val keyDarkModeEnabled = booleanPreferencesKey("darkModeEnabled")
private val keyInAppReviewCounter = intPreferencesKey("inAppReviewCounter")
//private const val KEY_MEASUREMENT_SYSTEM = "measurementSystem"

object AppSettings : KotprefModel() {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    var measurementSystem by intPref(default = MEASUREMENT_SYSTEM_ORIGINAL)

    // Dark Mode
    val darkModeEnabledFlow: Flow<Boolean> = context
        .dataStore.data.map { preferences ->
            preferences[keyDarkModeEnabled] ?: false
        }

    suspend fun setDarkModeEnabled(isEnabled: Boolean) =
        withContext(ioDispatcher) {
            context.dataStore.edit { preferences ->
                preferences[keyDarkModeEnabled] = isEnabled
            }
        }

    //In app reviews
    val inAppReviewCounterFlow: Flow<Int> = context
        .dataStore.data.map { preferences ->
            preferences[keyInAppReviewCounter] ?: 0
        }


    suspend fun resetInAppReviewCounter() =
        withContext(ioDispatcher) {
            context.dataStore.edit { preferences ->
                preferences[keyInAppReviewCounter] = 0
            }
        }

    suspend fun incrementInAppReviewCounter() =
        withContext(ioDispatcher) {
            context.dataStore.edit { preferences ->
                val currentValue: Int = preferences[keyInAppReviewCounter] ?: 0
                preferences[keyInAppReviewCounter] = currentValue + 1
            }
        }
}
