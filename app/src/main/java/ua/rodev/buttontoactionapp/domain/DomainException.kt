package ua.rodev.buttontoactionapp.domain

sealed class DomainException : IllegalStateException() {
    object NoInternetConnection : DomainException()
    object ServiceUnavailable : DomainException()
    object WrongActionType : DomainException()
    data class ActionOnCoolDown(val action: String) : DomainException()
}
