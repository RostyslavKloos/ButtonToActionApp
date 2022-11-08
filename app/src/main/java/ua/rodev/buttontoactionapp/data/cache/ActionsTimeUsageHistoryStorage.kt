package ua.rodev.buttontoactionapp.data.cache

interface ActionsTimeUsageHistoryStorage {

    interface Save {
        suspend fun save(data: HashMap<String, Long>)
    }

    interface Read {
        suspend fun read(): HashMap<String, Long>
    }

    interface Mutable : Save, Read

    class Main(private val preferences: PreferenceDataStore) : Mutable {

        override suspend fun save(data: HashMap<String, Long>) = preferences.save(KEY, data)
        override suspend fun read() = preferences.read(KEY)

        companion object {
            private const val KEY = "ActionsCoolDownKey"
        }
    }
}
