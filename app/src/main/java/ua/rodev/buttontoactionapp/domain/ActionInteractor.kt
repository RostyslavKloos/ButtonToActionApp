package ua.rodev.buttontoactionapp.domain

interface ActionInteractor {

    suspend fun action(currentTimeMills: Long): ActionResult

    // TODO: the “Toast message action” should only be chosen if there is an internet connection.
    class Main(
        private val repository: ActionRepository,
        private val handleError: HandleError<String>,
        private val checkValidDays: CheckValidDays,
        private val usageHistory: ActionsTimeUsageHistoryStorage.Mutable,
        private val mapper: ActionDomain.Mapper<ActionResult>,
    ) : ActionInteractor {

        override suspend fun action(currentTimeMills: Long): ActionResult {
            try {
                val actions = repository.fetchActions()
                val coolDownMap = usageHistory.read()
                val availableActions = actions
                    .filter { it.canBeChosen() && it.checkValidDays(checkValidDays) }
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
