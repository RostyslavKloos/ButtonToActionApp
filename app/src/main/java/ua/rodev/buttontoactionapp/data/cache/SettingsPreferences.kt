package ua.rodev.buttontoactionapp.data.cache

import android.content.Context

class SettingsPreferences(context: Context) : AbstractPreferenceDataStore<Boolean>(context) {

    override fun save(key: String, data: Boolean) {
        preferences.edit().putBoolean(key, data).apply()
    }

    override fun read(key: String): Boolean = preferences.getBoolean(key, false)
}