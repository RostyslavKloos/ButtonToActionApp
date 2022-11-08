package ua.rodev.buttontoactionapp.data

import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.core.ManageResources
import ua.rodev.buttontoactionapp.data.cache.ActionsTimeUsageHistoryStorage
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
    // TODO: add unit tests, then refactor 
    override suspend fun action(): ActionsResult {
        try {
            val actions = repository.fetchActions()
            val availableActions = actions.filter {
                it.canBeChosen() && it.checkValidDays(checkValidDays)
            }
            if (availableActions.isEmpty())
                return ActionsResult.Failure(manageResources.string(R.string.no_available_actions))
            var priorityAction = availableActions.first()
            availableActions.forEach { action ->
                if (action.higherPriorityThan(priorityAction)) priorityAction = action
            }
            return findActionWithoutCoolDown.find(priorityAction)
        } catch (e: DomainException) {
            return ActionsResult.Failure(handleError.handle(e))
        }
    }
}

// TODO: 1.rename. 2.unit tests, 3. refactor 4.remove somewhere
interface FindActionWithoutCoolDown {

    suspend fun find(priorityAction: ActionDomain): ActionsResult

    class Main(
        private val mapper: ActionDomain.Mapper<ActionsResult>,
        private val usageHistory: ActionsTimeUsageHistoryStorage.Mutable,
        private val handleError: HandleError<String>,
    ) : FindActionWithoutCoolDown {
        override suspend fun find(priorityAction: ActionDomain): ActionsResult {
            val coolDownMap = usageHistory.read()
            val resultActionLastUsageTime = priorityAction.findInMapByType(coolDownMap)
            val currentTimeMillis = System.currentTimeMillis()
            return if (resultActionLastUsageTime == null) {
                priorityAction.updateTimeUsage(coolDownMap, currentTimeMillis)
                usageHistory.save(coolDownMap)
                priorityAction.map(mapper)
            } else {
                if (priorityAction.onCoolDown(currentTimeMillis, resultActionLastUsageTime)) {
                    ActionsResult.Failure(handleError.handle(priorityAction.mapToDomainException()))
                } else {
                    priorityAction.updateTimeUsage(coolDownMap, currentTimeMillis)
                    usageHistory.save(coolDownMap)
                    priorityAction.map(mapper)
                }
            }
        }
    }
}
