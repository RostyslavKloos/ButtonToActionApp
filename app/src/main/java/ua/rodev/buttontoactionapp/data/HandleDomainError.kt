package ua.rodev.buttontoactionapp.data

import ua.rodev.buttontoactionapp.domain.DomainException
import ua.rodev.buttontoactionapp.domain.HandleError
import java.net.UnknownHostException

class HandleDomainError : HandleError<Exception> {

    override fun handle(e: Exception) = when (e) {
        is UnknownHostException -> DomainException.NoInternetConnection
        is DomainException -> e
        else -> DomainException.ServiceUnavailable
    }
}
