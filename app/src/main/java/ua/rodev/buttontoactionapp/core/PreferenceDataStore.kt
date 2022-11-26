package ua.rodev.buttontoactionapp.core

interface PreferenceDataStore<T> {
    fun save(key: String, data: T)
    fun read(key: String): T
}
