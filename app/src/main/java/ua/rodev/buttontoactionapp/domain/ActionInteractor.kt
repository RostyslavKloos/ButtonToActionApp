package ua.rodev.buttontoactionapp.domain

interface ActionInteractor {

    suspend fun action(): ActionsResult

}
