package ua.rodev.buttontoactionapp.presentation

import androidx.fragment.app.Fragment
import ua.rodev.buttontoactionapp.presentation.action.ActionFragment
import ua.rodev.buttontoactionapp.presentation.contacts.ContactsFragment

sealed class Screen {

    abstract fun fragment(): Class<out Fragment>

    object Action : Screen() {
        override fun fragment(): Class<out Fragment> = ActionFragment::class.java
    }

    object Contacts : Screen() {
        override fun fragment(): Class<out Fragment> = ContactsFragment::class.java
    }

}