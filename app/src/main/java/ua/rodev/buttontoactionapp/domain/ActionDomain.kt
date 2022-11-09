package ua.rodev.buttontoactionapp.domain

import ua.rodev.buttontoactionapp.data.CheckValidDays

// TODO: refactor methods
data class ActionDomain(
    private val type: ActionType,
    private val enabled: Boolean,
    private val priority: Int,
    private val validDays: List<Int>,
    private val coolDown: Long,
) {

    interface Mapper<T> {
        fun map(type: ActionType): T
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(type)

    fun canBeChosen() = enabled

    fun checkValidDays(checkValidDays: CheckValidDays): Boolean = checkValidDays.check(validDays)

    fun higherPriorityThan(source: ActionDomain): Boolean = this.priority < source.priority

    fun findInMapByType(map: Map<String, Long>): Long? = map[type.value]

    fun updateTimeUsage(map: MutableMap<String, Long>, time: Long) {
        map[type.value] = time
    }

    fun onCoolDown(currentTimeMillis: Long, resultActionLastUsageTime: Long): Boolean {
        return (currentTimeMillis - coolDown) < resultActionLastUsageTime
    }

    fun mapToDomainException(): DomainException.ActionOnCoolDown =
        DomainException.ActionOnCoolDown(type.value)
}
