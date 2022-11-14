package ua.rodev.buttontoactionapp.data

import org.joda.time.DateTimeUtils
import ua.rodev.buttontoactionapp.data.cache.ActionsTimeUsageHistoryStorage
import ua.rodev.buttontoactionapp.domain.ActionDomain
import ua.rodev.buttontoactionapp.domain.ActionResult
import ua.rodev.buttontoactionapp.domain.HandleError

interface FindActionWithoutCoolDown {

    suspend fun action(priorityAction: ActionDomain): ActionResult

    class Main(
        private val mapper: ActionDomain.Mapper<ActionResult>,
        private val usageHistory: ActionsTimeUsageHistoryStorage.Mutable,
        private val handleError: HandleError<String>,
    ) : FindActionWithoutCoolDown {
        override suspend fun action(priorityAction: ActionDomain): ActionResult {
            val coolDownMap = usageHistory.read()
            val resultActionLastUsageTime = priorityAction.findInMapByType(coolDownMap)
            val currentTimeMillis = DateTimeUtils.currentTimeMillis()
            return if (resultActionLastUsageTime == null) {
                val updated = priorityAction.updateTimeUsage(coolDownMap, currentTimeMillis)
                usageHistory.save(updated)
                priorityAction.map(mapper)
            } else {
                if (priorityAction.onCoolDown(currentTimeMillis, resultActionLastUsageTime)) {
                    ActionResult.Failure(handleError.handle(priorityAction.mapToDomainException()))
                } else {
                    val updated = priorityAction.updateTimeUsage(coolDownMap, currentTimeMillis)
                    usageHistory.save(updated)
                    priorityAction.map(mapper)
                }
            }
        }
    }
}
