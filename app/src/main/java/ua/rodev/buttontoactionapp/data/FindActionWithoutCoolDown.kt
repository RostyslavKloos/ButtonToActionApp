package ua.rodev.buttontoactionapp.data

import ua.rodev.buttontoactionapp.data.cache.ActionsTimeUsageHistoryStorage
import ua.rodev.buttontoactionapp.domain.ActionDomain
import ua.rodev.buttontoactionapp.domain.ActionsResult
import ua.rodev.buttontoactionapp.domain.HandleError

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