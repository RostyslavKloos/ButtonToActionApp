package ua.rodev.buttontoactionapp.data

import ua.rodev.buttontoactionapp.data.cloud.ActionCloud

interface FetchActions {
    suspend fun fetchActions(): List<ActionCloud>
}
