package ua.rodev.buttontoactionapp.domain

import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.core.ManageResources

interface HandleError<T> {

    fun handle(e: Exception): T

    class Ui(private val manageResources: ManageResources) : HandleError<String> {

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
}
