package ua.rodev.buttontoactionapp.data.cache

import android.content.Context
import com.google.gson.Gson

interface PreferenceDataStore {

    suspend fun save(key: String, data: HashMap<String, Long>)

    suspend fun read(key: String): HashMap<String, Long>

    class Base(
        private val gson: Gson = Gson(),
        context: Context,
    ) : PreferenceDataStore {

//        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "appDataStore")
//        private val dataStore = context.dataStore
        private val preferences = context.getSharedPreferences("appDataStore", Context.MODE_PRIVATE)

        override suspend fun save(key: String, data: HashMap<String, Long>) {
            val json = gson.toJson(data)
            preferences.edit().putString(key, json).apply()
//            dataStore.edit { preferences ->
//                preferences[stringPreferencesKey(key)] = json
//            }
        }

        override suspend fun read(key: String): HashMap<String, Long> {
//            val json = dataStore.data.firstOrNull()?.get(stringPreferencesKey(key))
//            Log.e("RORO", "READ ${gson.fromJson(json, HashMap::class.java)}")
//            return gson.fromJson(json, HashMap::class.java) as? HashMap<String, Double> ?: hashMapOf()
            val json = preferences.getString(key, "") ?: ""
            val hashMap = gson.fromJson(json, HashMap::class.java) as? HashMap<String, Double>
            val mapped = hashMapOf<String, Long>()
            hashMap?.toMutableMap()?.forEach {
                mapped[it.key] = it.value.toLong()
            }
            return mapped ?: hashMapOf()
        }
    }
}
