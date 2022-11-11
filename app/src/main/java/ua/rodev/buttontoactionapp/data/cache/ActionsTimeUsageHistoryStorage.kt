package ua.rodev.buttontoactionapp.data.cache

import ua.rodev.buttontoactionapp.core.Read
import ua.rodev.buttontoactionapp.core.Save

interface ActionsTimeUsageHistoryStorage {

    interface Mutable : Save<HashMap<String, Long>>, Read<HashMap<String, Long>>

    class Main(private val preferences: PreferenceDataStore<HashMap<String, Long>>) : Mutable {

        override fun save(data: HashMap<String, Long>) = preferences.save(KEY, data)
        override fun read() = preferences.read(KEY)

        companion object {
            private const val KEY = "ActionsCoolDownKey"
        }
    }
}
