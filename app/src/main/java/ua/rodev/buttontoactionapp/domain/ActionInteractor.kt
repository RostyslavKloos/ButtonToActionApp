package ua.rodev.buttontoactionapp.domain

import ua.rodev.buttontoactionapp.core.NetworkMonitor

interface ActionInteractor {

    suspend fun action(currentTimeMills: Long): ActionResult

    class Main(
        private val repository: ActionRepository,
        private val handleError: HandleError<String>,
        private val checkValidDays: CheckValidDays,
        private val usageHistory: ActionsTimeUsageHistoryStorage.Mutable,
        private val mapper: ActionDomain.Mapper<ActionResult>,
        private val networkMonitor: NetworkMonitor,
    ) : ActionInteractor {

        override suspend fun action(currentTimeMills: Long): ActionResult {
            try {
                val actions = repository.fetchActions()
                val isOnline = networkMonitor.isOnline()
                val coolDownMap = usageHistory.read()
                val availableActions = actions
                    .filter {
                        it.isEnabled()
                                && it.checkValidDays(checkValidDays)
                                && !it.cantBeChosen(isOnline)
                    }
                    .filter {
                        val lastTimeUsage = it.findInMapByType(coolDownMap)
                        lastTimeUsage == null || !it.onCoolDown(currentTimeMills, lastTimeUsage)
                    }
                if (availableActions.isEmpty())
                    return ActionResult.Failure(handleError.handle(DomainException.NoAvailableActions))
                var priorityAction = availableActions.first()
                availableActions.forEach { action ->
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
