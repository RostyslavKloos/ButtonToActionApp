package ua.rodev.buttontoactionapp.data

import ua.rodev.buttontoactionapp.ActionType
import ua.rodev.buttontoactionapp.domain.ActionInteractor
import ua.rodev.buttontoactionapp.domain.ActionRepository
import ua.rodev.buttontoactionapp.presentation.action.ActionDomainToUiMapper
import javax.inject.Inject

// TODO rewrite logic and use cool down period (long) with valid days (days array)
class ActionInteractorImpl @Inject constructor(
    private val repository: ActionRepository,
    private val mapper: ActionDomainToUiMapper,
) : ActionInteractor {
    override suspend fun action(): ActionType {
        val data = repository.fetchActions()
        val availableActions = data.filter {
            it.canBeChosen()
        }
        var priorityAction = availableActions.first()
        availableActions.forEach { action ->
            if (priorityAction.priority < action.priority) priorityAction = action
        }

        // TODO return type should be ActionUi
        return priorityAction.map(mapper)
    }
}
