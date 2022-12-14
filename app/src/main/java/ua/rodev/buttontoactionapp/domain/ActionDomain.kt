package ua.rodev.buttontoactionapp.domain

// TODO: refactor methods
data class ActionDomain(
    private val type: ActionType,
    private val enabled: Boolean,
    private val priority: Int,
    private val validDays: List<Int>,
    private val coolDown: Long,
) {

    fun mapToSuccessResult(): ActionResult.Success = ActionResult.Success(type)

    fun isEnabled(): Boolean = enabled

    fun isToastAction(): Boolean = type is ActionType.Toast

    fun checkValidDays(checkValidDays: CheckValidDays): Boolean = checkValidDays.isValid(validDays)

    fun higherPriorityThan(source: ActionDomain): Boolean = this.priority > source.priority

    fun lastUsageTime(map: Map<String, Long>): Long? = map[type.value]

    fun updatedUsageTime(map: Map<String, Long>, time: Long): Map<String, Long> {
        return map.toMutableMap().apply { this[type.value] = time }
    }

    fun onCoolDown(currentTimeMillis: Long, resultActionLastUsageTime: Long): Boolean {
        return (currentTimeMillis - coolDown) < resultActionLastUsageTime
    }

}
