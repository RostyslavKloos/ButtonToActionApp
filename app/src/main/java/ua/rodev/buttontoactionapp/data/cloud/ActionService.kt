package ua.rodev.buttontoactionapp.data.cloud

import retrofit2.http.GET

interface ActionService {

    @GET("androidexam/butto_to_action_config.json")
    suspend fun fetchActions(): List<ActionCloud>
}
