package ua.rodev.buttontoactionapp.domain

import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.core.ManageResources

interface ActionInteractor {

    suspend fun action(currentTimeMills: Long): ActionResult

    // TODO: the “Toast message action” should only be chosen if there is an internet connection.
    class Main(
        private val repository: ActionRepository,
        private val handleError: HandleError<String>,
        private val manageResources: ManageResources,
        private val checkValidDays: CheckValidDays,
        private val usageHistory: ActionsTimeUsageHistoryStorage.Mutable,
        private val mapper: ActionDomain.Mapper<ActionResult>,
    ) : ActionInteractor {

        override suspend fun action(currentTimeMills: Long): ActionResult {
            try {
                val actions = repository.fetchActions()
                val availableActions = actions.filter {
                    it.canBeChosen() && it.checkValidDays(checkValidDays)
                }
                if (availableActions.isEmpty())
                    return ActionResult.Failure(manageResources.string(R.string.no_available_actions))
                val coolDownMap = usageHistory.read()
                val actionsWithoutCoolDown = availableActions.filter {
                    val lastTimeUsageMills = it.findInMapByType(coolDownMap)
                    lastTimeUsageMills == null || !it.onCoolDown(currentTimeMills, lastTimeUsageMills)
                }
                if (actionsWithoutCoolDown.isEmpty())
                    return ActionResult.Failure(manageResources.string(R.string.no_available_actions))
                var priorityAction = actionsWithoutCoolDown.first()
                actionsWithoutCoolDown.forEach { action ->
                    if (action.higherPriorityThan(priorityAction)) priorityAction = action
                }
                usageHistory.save(priorityAction.updateTimeUsage(coolDownMap, currentTimeMills))
                return priorityAction.map(mapper)
            } catch (e: DomainException) {
                return ActionResult.Failure(handleError.handle(e))
            }
        }
    }
}
