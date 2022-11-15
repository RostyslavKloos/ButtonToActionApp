package ua.rodev.buttontoactionapp.presentation

import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.core.ManageResources
import ua.rodev.buttontoactionapp.domain.DomainException
import ua.rodev.buttontoactionapp.domain.HandleError

class HandleUiError(private val manageResources: ManageResources) : HandleError<String> {

    override fun handle(e: Exception): String {
        return when (e) {
            is DomainException.NoInternetConnection -> manageResources.string(R.string.no_connection_message)
            is DomainException.ActionOnCoolDown -> {
                manageResources.string(R.string.error_action_on_cool_down, e.action)
            }
            else -> manageResources.string(R.string.service_is_unavailable)
        }
    }
}
