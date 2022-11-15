package ua.rodev.buttontoactionapp.domain

interface PreferenceDataStore<T> {

    fun save(key: String, data: T)

    fun read(key: String): T

}
