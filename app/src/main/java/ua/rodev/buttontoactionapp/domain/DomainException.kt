package ua.rodev.buttontoactionapp.domain

sealed class DomainException : IllegalStateException() {
    object NoInternetConnection : DomainException()
    object ServiceUnavailable : DomainException()
    object WrongActionType : DomainException()
    object NoAvailableActions : DomainException()
    data class ActionOnCoolDown(val action: String) : DomainException()
}
