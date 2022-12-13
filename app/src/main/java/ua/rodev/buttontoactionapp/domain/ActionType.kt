package ua.rodev.buttontoactionapp.domain

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.presentation.action.HandleAction

// TODO: make value private 
@Parcelize
sealed class ActionType(val value: String) : Parcelable {

    abstract fun handle(actionUi: HandleAction)

    object Animation : ActionType("animation") {
        override fun handle(actionUi: HandleAction) = actionUi.showAnimation()
    }

    data class Toast(
        @StringRes private val messageId: Int = R.string.action_is_toast,
    ) : ActionType("toast") {
        override fun handle(actionUi: HandleAction) = actionUi.showToast(messageId)
    }

    object Notification : ActionType("notification") {
        override fun handle(actionUi: HandleAction) = actionUi.showNotification()
    }

    object Call : ActionType("call") {
        override fun handle(actionUi: HandleAction) = actionUi.call()
    }

    object None : ActionType("") {
        override fun handle(actionUi: HandleAction) = Unit
    }
}
