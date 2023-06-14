package com.chmurka.codzisnaobiad.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class Settings(
    val darkTheme : Boolean = false,
)

class DataStoreRepository(private val dataStore : DataStore<Preferences>) {
    private object PreferencesKeys {
        val DARK_THEME = booleanPreferencesKey("dark_theme")
    }

    suspend fun setDarkTheme(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_THEME] = value
        }
    }

    val settingsFlow : Flow<Settings> = dataStore.data.map { preferences ->
            val darkTheme = preferences[PreferencesKeys.DARK_THEME] ?: false
            Settings(darkTheme)
        }
}