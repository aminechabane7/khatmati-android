package com.example.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "khatmati_preferences")

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val QURAN_FONT_SIZE = floatPreferencesKey("quran_font_size")
        val QURAN_READING_MODE = stringPreferencesKey("quran_reading_mode") // "mushaf" or "ayah"
        val SELECTED_RECITER_ID = stringPreferencesKey("selected_reciter_id")
        val THEME_MODE = stringPreferencesKey("theme_mode") // "system", "light", "dark"
    }

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.ONBOARDING_COMPLETED] ?: false
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] = completed
        }
    }

    val quranFontSize: Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.QURAN_FONT_SIZE] ?: 24f
    }

    suspend fun setQuranFontSize(size: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.QURAN_FONT_SIZE] = size
        }
    }

    val quranReadingMode: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.QURAN_READING_MODE] ?: "mushaf"
    }

    suspend fun setQuranReadingMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.QURAN_READING_MODE] = mode
        }
    }

    val themeMode: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.THEME_MODE] ?: "system"
    }

    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = mode
        }
    }
}
