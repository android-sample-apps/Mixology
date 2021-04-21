package com.yanivsos.mixological.v2.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val keyDarkModeEnabled = booleanPreferencesKey("darkModeEnabled")
private val keyInAppReviewCounter = intPreferencesKey("inAppReviewCounter")
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "appSettings")

class AppDataStore(private val context: Context) {

    // Dark Mode
    val darkModeEnabledFlow: Flow<Boolean> = context
        .dataStore.data.map { preferences ->
            preferences[keyDarkModeEnabled] ?: false
        }

    suspend fun setDarkModeEnabled(isEnabled: Boolean) =
        context.dataStore.edit { preferences ->
            preferences[keyDarkModeEnabled] = isEnabled
        }

    //In app reviews
    val inAppReviewCounterFlow: Flow<Int> = context
        .dataStore.data.map { preferences ->
            preferences[keyInAppReviewCounter] ?: 0
        }


    suspend fun resetInAppReviewCounter() =
        context.dataStore.edit { preferences ->
            preferences[keyInAppReviewCounter] = 0
        }

    suspend fun incrementInAppReviewCounter() =
        context.dataStore.edit { preferences ->
            val currentValue: Int = preferences[keyInAppReviewCounter] ?: 0
            preferences[keyInAppReviewCounter] = currentValue + 1
        }
}
