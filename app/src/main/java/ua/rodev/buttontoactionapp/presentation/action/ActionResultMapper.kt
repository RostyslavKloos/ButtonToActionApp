package ua.rodev.buttontoactionapp.presentation.action

import ua.rodev.buttontoactionapp.core.Log
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.domain.ActionsResult
import ua.rodev.buttontoactionapp.presentation.Communication

class ActionResultMapper(
    private val actionFlow: Communication.Mutable<ActionType>,
    private val log: Log
): ActionsResult.ActionResultMapper<Unit> {
    override suspend fun map(type: ActionType, errorMessage: String) {
        if (errorMessage.isEmpty()) {
            actionFlow.map(type)
            log.print("ACTIN $type")
        } else {
            log.print("ERROR MESSAGE $errorMessage")
        }
    }
}