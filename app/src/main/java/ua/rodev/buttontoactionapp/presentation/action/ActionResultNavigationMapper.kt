package ua.rodev.buttontoactionapp.presentation.action

import ua.rodev.buttontoactionapp.core.Log
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.domain.ActionsResult
import ua.rodev.buttontoactionapp.presentation.Communication
import ua.rodev.buttontoactionapp.presentation.NavigationStrategy
import ua.rodev.buttontoactionapp.presentation.Screen

class ActionResultNavigationMapper(
    private val navigationFlow: Communication.Update<NavigationStrategy>,
    private val actionFlow: Communication.Mutable<ActionType>,
    private val log: Log,
) : ActionsResult.ActionResultMapper<Unit> {
    override suspend fun map(type: ActionType, errorMessage: String) {
        if (errorMessage.isEmpty()) {
            if (type == ActionType.Call) {
                navigationFlow.map(NavigationStrategy.Add(Screen.Contacts))
            } else {
                actionFlow.map(type)
                log.print("ACTION $type")
            }

        } else {
            log.print("ERROR MESSAGE $errorMessage")
        }
    }
}
