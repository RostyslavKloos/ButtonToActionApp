package ua.rodev.buttontoactionapp.presentation.action

import ua.rodev.buttontoactionapp.core.Log
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.domain.ActionResult
import ua.rodev.buttontoactionapp.presentation.Target

class ActionResultMapper(
    private val actionTarget: Target.Mutable<ActionType>,
    private val log: Log,
) : ActionResult.ActionResultMapper<Unit> {
    override suspend fun map(type: ActionType, errorMessage: String) {
        if (errorMessage.isEmpty()) {
            actionTarget.map(type)
            log.print("ACTION $type")
        } else {
            log.print("ERROR MESSAGE $errorMessage")
        }
    }
}
