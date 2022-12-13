package ua.rodev.buttontoactionapp.data.cache

import ua.rodev.buttontoactionapp.core.PreferenceDataStore
import ua.rodev.buttontoactionapp.domain.ActionsUsageTimeHistoryStorage

class MainActionsUsageTimeHistoryStorage(
    private val preferences: PreferenceDataStore<Map<String, Long>>,
) : ActionsUsageTimeHistoryStorage.Mutable {

    override fun save(data: Map<String, Long>) = preferences.save(KEY, data)
    override fun read() = preferences.read(KEY)

    companion object {
        private const val KEY = "actionsCoolDownKey"
    }
}
