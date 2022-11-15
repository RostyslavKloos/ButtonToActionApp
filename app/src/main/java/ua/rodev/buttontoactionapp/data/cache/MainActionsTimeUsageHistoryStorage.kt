package ua.rodev.buttontoactionapp.data.cache

import ua.rodev.buttontoactionapp.domain.ActionsTimeUsageHistoryStorage
import ua.rodev.buttontoactionapp.domain.PreferenceDataStore

class MainActionsTimeUsageHistoryStorage(
    private val preferences: PreferenceDataStore<Map<String, Long>>,
) : ActionsTimeUsageHistoryStorage.Mutable {

    override fun save(data: Map<String, Long>) = preferences.save(KEY, data)
    override fun read() = preferences.read(KEY)

    companion object {
        private const val KEY = "actionsCoolDownKey"
    }
}