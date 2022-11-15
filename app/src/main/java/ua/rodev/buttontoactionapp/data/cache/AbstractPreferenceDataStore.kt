package ua.rodev.buttontoactionapp.data.cache

import android.content.Context
import android.content.SharedPreferences
import ua.rodev.buttontoactionapp.domain.PreferenceDataStore

abstract class AbstractPreferenceDataStore<T>(context: Context) : PreferenceDataStore<T> {
    protected val preferences: SharedPreferences = context.getSharedPreferences(
        preferencesName,
        Context.MODE_PRIVATE
    )

    companion object {
        private const val preferencesName = "appPreferences"
    }
}
