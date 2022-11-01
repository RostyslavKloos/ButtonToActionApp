package ua.rodev.buttontoactionapp.data.remote

import retrofit2.http.GET

interface ActionService {

    @GET("androidexam/butto_to_action_config.json")
    suspend fun fetchActions(): List<ActionRemote>
}