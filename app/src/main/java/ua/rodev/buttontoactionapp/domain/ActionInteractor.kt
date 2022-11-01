package ua.rodev.buttontoactionapp.domain

import ua.rodev.buttontoactionapp.ActionType

interface ActionInteractor {

    suspend fun action(): ActionType

}