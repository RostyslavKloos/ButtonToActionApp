package ua.rodev.buttontoactionapp.presentation

import ua.rodev.buttontoactionapp.domain.ActionDomain
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.domain.ActionResult

class ActionDomainToActionResultMapper : ActionDomain.Mapper<ActionResult> {
    override fun map(type: ActionType) = ActionResult.Success(type)
}
