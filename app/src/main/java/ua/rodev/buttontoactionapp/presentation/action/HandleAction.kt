package ua.rodev.buttontoactionapp.presentation.action

import androidx.annotation.StringRes

interface HandleAction {
    fun showAnimation()
    fun showToast(@StringRes messageId: Int)
    fun call()
    fun showNotification()
}
