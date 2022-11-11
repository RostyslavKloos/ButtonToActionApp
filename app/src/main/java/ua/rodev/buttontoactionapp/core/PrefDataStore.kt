//package ua.rodev.buttontoactionapp.core
//
//import android.content.Context
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.core.booleanPreferencesKey
//import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.preferencesDataStore
//import kotlinx.coroutines.flow.firstOrNull
//
//
//interface PrefDataStore<T> {
//    suspend fun <T> read(key: Preferences.Key<T>, defaultValue: T): T
//    suspend fun <T> save(key: Preferences.Key<T>, value: T)
//    suspend fun <T> removePreference(key: Preferences.Key<T>)
//    suspend fun clearAllPreference()
//
//    abstract class Abstract<T>(context: Context) : PrefDataStore<T> {
//        protected val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = dataStoreName)
//        private val dataSource = context.dataStore
//
//        override suspend fun <T> read(key: Preferences.Key<T>, defaultValue: T): T {
//            return dataSource.data.firstOrNull()?.get(key) ?: defaultValue
//        }
//
//        override suspend fun <T> save(key: Preferences.Key<T>, value: T) {
//            dataSource.edit { preferences ->
//                preferences[key] = value
//            }
//        }
//
//        override suspend fun <T> removePreference(key: Preferences.Key<T>) {
//            dataSource.edit {
//                it.remove(key)
//            }
//        }
//
//        override suspend fun clearAllPreference() {
//            dataSource.edit { preferences ->
//                preferences.clear()
//            }
//        }
//
//        companion object {
//            private const val dataStoreName = "appDataStore"
//        }
//    }
//
//    class ScreenConfigurations(context: Context) : Abstract<Boolean>(context)
//}
//
//interface Save<T> {
//    suspend fun save(data: T)
//}
//
//interface Read<T> {
//    suspend fun read(): T
//}
//
//interface Mutable<T> : Save<T>, Read<T>
//
//interface ActionScreenConfiguration : Mutable<Boolean> {
//
//    class Main(private val preferences: PrefDataStore<Boolean>) : ActionScreenConfiguration {
//
//        private val preferenceKey = booleanPreferencesKey(KEY)
//
//        override suspend fun save(data: Boolean) = preferences.save(preferenceKey, data)
//
//        override suspend fun read() = preferences.read(preferenceKey, false)
//
//        companion object {
//            private const val KEY = "ActionsCoolDownKey"
//        }
//    }
//}
