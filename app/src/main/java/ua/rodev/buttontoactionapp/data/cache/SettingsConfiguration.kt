package ua.rodev.buttontoactionapp.data.cache

import ua.rodev.buttontoactionapp.core.Read
import ua.rodev.buttontoactionapp.core.Save
import ua.rodev.buttontoactionapp.core.PreferenceDataStore

interface SettingsConfiguration {

    interface Mutable : Save<Boolean>, Read<Boolean>

    abstract class Settings(
        private val preferences: PreferenceDataStore<Boolean>,
    ) : Mutable {
        abstract val key: String
        override fun save(data: Boolean) = preferences.save(key, data)
        override fun read(): Boolean = preferences.read(key)
    }

    class UseComposePreferencesWrapper(
        preferences: PreferenceDataStore<Boolean>,
        override val key: String = "useComposeKey",
    ) : Settings(preferences)

    class UseContactsScreenPreferencesWrapper(
        preferences: PreferenceDataStore<Boolean>,
        override val key: String = "useContactsScreen",
    ) : Settings(preferences)
}
