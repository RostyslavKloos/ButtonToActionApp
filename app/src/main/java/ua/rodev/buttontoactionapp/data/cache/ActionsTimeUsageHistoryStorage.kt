package ua.rodev.buttontoactionapp.data.cache

import ua.rodev.buttontoactionapp.core.Read
import ua.rodev.buttontoactionapp.core.Save

interface ActionsTimeUsageHistoryStorage {

    interface Mutable : Save<Map<String, Long>>, Read<Map<String, Long>>

    class Main(private val preferences: PreferenceDataStore<Map<String, Long>>) : Mutable {

        override fun save(data: Map<String, Long>) = preferences.save(KEY, data)
        override fun read() = preferences.read(KEY)

        companion object {
            private const val KEY = "actionsCoolDownKey"
        }
    }
}
