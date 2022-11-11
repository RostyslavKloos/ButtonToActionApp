package ua.rodev.buttontoactionapp.data.cache

import ua.rodev.buttontoactionapp.core.Read
import ua.rodev.buttontoactionapp.core.Save

interface ActionScreenTypeConfiguration {

    interface Mutable : Save<Boolean>, Read<Boolean>

    class Main(private val preferences: PreferenceDataStore<Boolean>) : Mutable {

        override fun save(data: Boolean) = preferences.save(KEY, data)
        override fun read(): Boolean = preferences.read(KEY)

        companion object {
            private const val KEY = "actionScreenTypeConfiguration"
        }
    }
}
