package ua.rodev.buttontoactionapp.presentation.action

import ua.rodev.buttontoactionapp.domain.ActionResult
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.presentation.Target

class ActionResultMapper(
    private val actionTarget: Target.Mutable<ActionType>,
    private val snackbarTarget: Target.Mutable<String>,
) : ActionResult.ActionResultMapper<Unit> {
    override suspend fun map(type: ActionType, errorMessage: String) {
        if (errorMessage.isEmpty())
            actionTarget.map(type)
        else
            snackbarTarget.map(errorMessage)
    }
}
