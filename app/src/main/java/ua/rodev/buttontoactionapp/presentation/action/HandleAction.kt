package ua.rodev.buttontoactionapp.presentation.action

interface HandleAction {
    fun showAnimation()
    fun showToast(message: String)
    fun call()
    fun showNotification()
}