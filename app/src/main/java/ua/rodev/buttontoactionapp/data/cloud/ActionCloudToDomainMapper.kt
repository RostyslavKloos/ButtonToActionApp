package ua.rodev.buttontoactionapp.data.cloud

import ua.rodev.buttontoactionapp.domain.ActionDomain
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.domain.DomainException

class ActionCloudToDomainMapper : ActionCloud.Mapper<ActionDomain> {
    @Throws(DomainException::class)
    override fun map(
        type: String,
        enabled: Boolean,
        priority: Int,
        validDays: List<Int>,
        coolDown: Long,
    ): ActionDomain {
        val actionType = ActionType.values().find {
            it.value == type
        } ?: throw DomainException.WrongActionType

        return ActionDomain(actionType, enabled, priority, validDays, coolDown)
    }
}
