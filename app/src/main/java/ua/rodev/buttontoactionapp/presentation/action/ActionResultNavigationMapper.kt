package ua.rodev.buttontoactionapp.presentation.action

import ua.rodev.buttontoactionapp.domain.ActionResult
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.presentation.NavigationStrategy
import ua.rodev.buttontoactionapp.presentation.Screen
import ua.rodev.buttontoactionapp.presentation.Target

class ActionResultNavigationMapper(
    private val navigationTarget: Target.Update<NavigationStrategy>,
    private val actionTarget: Target.Mutable<ActionType>,
    private val snackbarTarget: Target.Mutable<String>,
) : ActionResult.ActionResultMapper<Unit> {
    override suspend fun map(type: ActionType, errorMessage: String) {
        if (errorMessage.isEmpty()) {
            if (type == ActionType.Call)
                navigationTarget.map(NavigationStrategy.Add(Screen.Contacts))
            else
                actionTarget.map(type)
        } else {
            snackbarTarget.map(errorMessage)
        }
    }
}
