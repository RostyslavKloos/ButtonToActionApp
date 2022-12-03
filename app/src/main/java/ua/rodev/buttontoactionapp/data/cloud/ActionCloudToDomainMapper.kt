package ua.rodev.buttontoactionapp.data.cloud

import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.core.ManageResources
import ua.rodev.buttontoactionapp.domain.ActionDomain
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.domain.DomainException

class ActionCloudToDomainMapper(
    private val resources: ManageResources
) : ActionCloud.Mapper<ActionDomain> {
    @Throws(DomainException::class)
    override fun map(
        type: String,
        enabled: Boolean,
        priority: Int,
        validDays: List<Int>,
        coolDown: Long,
    ): ActionDomain {
        val toastAction = ActionType.Toast(resources.string(R.string.action_is_toast))
        val actionType = when (type) {
            toastAction.value -> toastAction
            ActionType.Notification.value -> ActionType.Notification
            ActionType.Call.value -> ActionType.Call
            ActionType.Animation.value -> ActionType.Animation
            else -> throw DomainException.WrongActionType
        }
        return ActionDomain(actionType, enabled, priority, validDays, coolDown)
    }
}
