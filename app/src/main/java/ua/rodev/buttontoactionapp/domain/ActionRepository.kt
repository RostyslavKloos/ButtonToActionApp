package ua.rodev.buttontoactionapp.domain

interface ActionRepository {
    suspend fun fetchActions(): List<ActionDomain>
}