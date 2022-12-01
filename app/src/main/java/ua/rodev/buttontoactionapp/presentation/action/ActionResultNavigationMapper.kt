package ua.rodev.buttontoactionapp.presentation.action

import ua.rodev.buttontoactionapp.core.Log
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.domain.ActionResult
import ua.rodev.buttontoactionapp.presentation.Target
import ua.rodev.buttontoactionapp.presentation.NavigationStrategy
import ua.rodev.buttontoactionapp.presentation.Screen

class ActionResultNavigationMapper(
    private val navigationTarget: Target.Update<NavigationStrategy>,
    private val actionTarget: Target.Mutable<ActionType>,
    private val log: Log,
) : ActionResult.ActionResultMapper<Unit> {
    override suspend fun map(type: ActionType, errorMessage: String) {
        if (errorMessage.isEmpty()) {
            if (type == ActionType.Call) {
                navigationTarget.map(NavigationStrategy.Add(Screen.Contacts))
            } else {
                actionTarget.map(type)
                log.print("ACTION $type")
            }

        } else {
            log.print("ERROR MESSAGE $errorMessage")
        }
    }
}
