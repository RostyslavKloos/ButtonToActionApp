package ua.rodev.buttontoactionapp.presentation.action

import androidx.lifecycle.ViewModel
import ua.rodev.buttontoactionapp.core.ViewModelModule
import ua.rodev.buttontoactionapp.data.cache.SettingsConfiguration
import ua.rodev.buttontoactionapp.presentation.action.main.MainActionViewModel
import ua.rodev.buttontoactionapp.presentation.action.withNavigation.ActionWithNavigationViewModel

interface DependencyContainer {

    fun <T : ViewModel> module(clasz: Class<T>): ViewModelModule<*>

    class Main(
        private val mainActionModule: ViewModelModule<MainActionViewModel>,
        private val actionWithNavigationModule: ViewModelModule<ActionWithNavigationViewModel>,
        private val useContactsScreen: SettingsConfiguration.Mutable,
    ) : DependencyContainer {

        override fun <T : ViewModel> module(clasz: Class<T>): ViewModelModule<*> {
            return if (useContactsScreen.read()) actionWithNavigationModule else mainActionModule
        }
    }
}
