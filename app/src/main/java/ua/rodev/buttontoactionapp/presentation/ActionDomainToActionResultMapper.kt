package ua.rodev.buttontoactionapp.presentation

import ua.rodev.buttontoactionapp.domain.ActionDomain
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.domain.ActionsResult

class ActionDomainToActionResultMapper : ActionDomain.Mapper<ActionsResult> {
    override fun map(type: ActionType) = ActionsResult.Success(type)
}
