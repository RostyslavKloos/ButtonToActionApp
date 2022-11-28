package ua.rodev.buttontoactionapp.presentation.action

interface HandleAction {
    fun showAnimation()
    fun showToast()
    fun call()
    fun showNotification()
}