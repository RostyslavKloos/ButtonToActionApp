package ua.rodev.buttontoactionapp.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ActionType(val value: String) : Parcelable {
    Animation("animation"),
    Toast("toast"),
    Call("call"),
    Notification("notification"),
    None("");
}
