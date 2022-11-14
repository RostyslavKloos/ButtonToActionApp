package ua.rodev.buttontoactionapp.data.cache

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

interface PreferenceDataStore<T> {

    fun save(key: String, data: T)

    fun read(key: String): T

    abstract class Abstract<T>(context: Context) : PreferenceDataStore<T> {
        protected val preferences: SharedPreferences = context.getSharedPreferences(
            preferencesName,
            Context.MODE_PRIVATE
        )

        companion object {
            private const val preferencesName = "appPreferences"
        }
    }

    class ActionsTimeHistoryUsageStore(
        private val gson: Gson = Gson(),
        context: Context,
    ) : Abstract<Map<String, Long>>(context) {

        override fun save(key: String, data: Map<String, Long>) {
            val json = gson.toJson(data)
            preferences.edit().putString(key, json).apply()
        }

        @Suppress("UNCHECKED_CAST")
        override fun read(key: String): Map<String, Long> {
            val json = preferences.getString(key, "") ?: ""
            val map = gson.fromJson(json, Map::class.java) as? Map<String, Double>
            val mapped = hashMapOf<String, Long>()
            map?.toMutableMap()?.forEach {
                mapped[it.key] = it.value.toLong()
            }
            return mapped
        }
    }

    class SettingsPreferences(context: Context) : Abstract<Boolean>(context) {

        override fun save(key: String, data: Boolean) {
            preferences.edit().putBoolean(key, data).apply()
        }

        override fun read(key: String): Boolean = preferences.getBoolean(key, false)
    }
}
