package ua.rodev.buttontoactionapp.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// TODO: make value private 
@Parcelize
enum class ActionType(val value: String) : Parcelable {
    Animation("animation"),
    Toast("toast"),
    Call("call"),
    Notification("notification"),
    None("");
}
