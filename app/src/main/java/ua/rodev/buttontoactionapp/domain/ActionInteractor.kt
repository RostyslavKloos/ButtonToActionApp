package ua.rodev.buttontoactionapp.domain

import ua.rodev.buttontoactionapp.core.NetworkMonitor

interface ActionInteractor {

    suspend fun actionResult(): ActionResult

    class Main(
        private val repository: ActionRepository,
        private val handleError: HandleError<String>,
        private val checkValidDays: CheckValidDays,
        private val usageHistory: ActionsUsageTimeHistoryStorage.Mutable,
        private val networkMonitor: NetworkMonitor,
        private val now: Now,
    ) : ActionInteractor {

        override suspend fun actionResult(): ActionResult {
            try {
                val actions = repository.fetchActions()
                val isOnline = networkMonitor.isOnline()
                val coolDownMap = usageHistory.read()
                val availableActions = actions
                    .filter {
                        it.isEnabled()
                                && it.checkValidDays(checkValidDays)
                                && !(it.isToastAction() && !isOnline)
                    }
                    .filter {
                        val lastUsageTime = it.lastUsageTime(coolDownMap)
                        lastUsageTime == null || !it.onCoolDown(now.time(), lastUsageTime)
                    }
                if (availableActions.isEmpty())
                    return ActionResult.Failure(handleError.handle(DomainException.NoAvailableActions))
                var priorityAction = availableActions.first()
                availableActions.forEach { action ->
                    if (action.higherPriorityThan(priorityAction)) priorityAction = action
                }
                usageHistory.save(priorityAction.updatedUsageTime(coolDownMap, now.time()))
                return priorityAction.mapToSuccessResult()
            } catch (e: DomainException) {
                return ActionResult.Failure(handleError.handle(e))
            }
        }
    }
}
