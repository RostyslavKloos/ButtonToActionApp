package ua.rodev.buttontoactionapp.presentation

import androidx.fragment.app.Fragment
import ua.rodev.buttontoactionapp.presentation.action.ActionFragment
import ua.rodev.buttontoactionapp.presentation.contacts.ContactsFragment
import ua.rodev.buttontoactionapp.presentation.settings.SettingsComposeFragment
import ua.rodev.buttontoactionapp.presentation.settings.SettingsFragment

sealed class Screen {

    abstract fun fragment(): Class<out Fragment>

    object Action : Screen() {
        override fun fragment(): Class<out Fragment> = ActionFragment::class.java
    }

    object Contacts : Screen() {
        override fun fragment(): Class<out Fragment> = ContactsFragment::class.java
    }

    object Settings : Screen() {
        override fun fragment(): Class<out Fragment> = SettingsFragment::class.java
    }

    object SettingsCompose : Screen() {
        override fun fragment(): Class<out Fragment> = SettingsComposeFragment::class.java
    }
}
