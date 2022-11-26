package ua.rodev.buttontoactionapp.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ua.rodev.buttontoactionapp.presentation.action.HandleAction

// TODO: make value private 
@Parcelize
sealed class ActionType(val value: String) : Parcelable {

    abstract fun handle(actionUi: HandleAction)

    object Animation : ActionType("animation") {
        override fun handle(actionUi: HandleAction) = actionUi.showAnimation()
    }

    object Toast : ActionType("toast") {
        override fun handle(actionUi: HandleAction) = actionUi.showToast()
    }

    object Notification : ActionType("notification") {
        override fun handle(actionUi: HandleAction) = actionUi.showNotification()
    }

    object Call : ActionType("call") {
        override fun handle(actionUi: HandleAction) = actionUi.call()
    }

    object None: ActionType("") {
        override fun handle(actionUi: HandleAction) = Unit
    }
}
