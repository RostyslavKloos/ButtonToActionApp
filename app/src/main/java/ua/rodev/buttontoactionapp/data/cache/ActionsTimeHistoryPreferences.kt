package ua.rodev.buttontoactionapp.data.cache

import android.content.Context
import com.google.gson.Gson

class ActionsTimeHistoryPreferences(
    private val gson: Gson = Gson(),
    context: Context,
) : AbstractPreferenceDataStore<Map<String, Long>>(context) {

    override fun save(key: String, data: Map<String, Long>) {
        val json = gson.toJson(data)
        preferences.edit().putString(key, json).apply()
    }

    @Suppress("UNCHECKED_CAST")
    override fun read(key: String): Map<String, Long> {
        val mapped = mutableMapOf<String, Long>()
        val json = preferences.getString(key, "") ?: return mapped
        val map = gson.fromJson(json, Map::class.java) as Map<String, Double>
        map.forEach {
            mapped[it.key] = it.value.toLong()
        }
        return mapped
    }
}