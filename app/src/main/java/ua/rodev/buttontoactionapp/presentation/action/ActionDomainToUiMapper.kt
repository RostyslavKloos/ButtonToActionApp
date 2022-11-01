package ua.rodev.buttontoactionapp.presentation.action

import ua.rodev.buttontoactionapp.ActionType
import ua.rodev.buttontoactionapp.domain.ActionDomain

class ActionDomainToUiMapper : ActionDomain.Mapper<ActionType> {

    override fun map(type: String): ActionType {
        return ActionType.fromType(type)
    }
}