package ua.rodev.buttontoactionapp.data

import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.core.ManageResources
import ua.rodev.buttontoactionapp.domain.*
import javax.inject.Inject

// TODO: the “Toast message action” should only be chosen if there is an internet connection. 
class MainActionInteractor @Inject constructor(
    private val repository: ActionRepository,
    private val handleError: HandleError<String>,
    private val manageResources: ManageResources,
    private val checkValidDays: CheckValidDays,
    private val findActionWithoutCoolDown: FindActionWithoutCoolDown
) : ActionInteractor {

    override suspend fun action(): ActionResult {
        try {
            val actions = repository.fetchActions()
            val availableActions = actions.filter {
                it.canBeChosen() && it.checkValidDays(checkValidDays)
            }
            if (availableActions.isEmpty())
                return ActionResult.Failure(manageResources.string(R.string.no_available_actions))
            var priorityAction = availableActions.first()
            availableActions.forEach { action ->
                if (action.higherPriorityThan(priorityAction)) priorityAction = action
            }
            return findActionWithoutCoolDown.action(priorityAction)
        } catch (e: DomainException) {
            return ActionResult.Failure(handleError.handle(e))
        }
    }
}
