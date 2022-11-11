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
    ) : Abstract<HashMap<String, Long>>(context) {

        override fun save(key: String, data: HashMap<String, Long>) {
            val json = gson.toJson(data)
            preferences.edit().putString(key, json).apply()
        }

        @Suppress("UNCHECKED_CAST")
        override fun read(key: String): HashMap<String, Long> {
            val json = preferences.getString(key, "") ?: ""
            val hashMap = gson.fromJson(json, HashMap::class.java) as? HashMap<String, Double>
            val mapped = hashMapOf<String, Long>()
            hashMap?.toMutableMap()?.forEach {
                mapped[it.key] = it.value.toLong()
            }
            return mapped
        }
    }

    class ActionScreenTypeConfiguration(context: Context) : Abstract<Boolean>(context) {

        override fun save(key: String, data: Boolean) {
            preferences.edit().putBoolean(key, data).apply()
        }

        override fun read(key: String): Boolean = preferences.getBoolean(key, false)
    }
}
