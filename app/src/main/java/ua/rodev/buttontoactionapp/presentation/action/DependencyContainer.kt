package ua.rodev.buttontoactionapp.presentation.action

import androidx.lifecycle.ViewModel
import ua.rodev.buttontoactionapp.core.ViewModelModule

interface DependencyContainer {

    fun <T : ViewModel> module(clasz: Class<T>, boolean: Boolean): ViewModelModule<*>

    class Main(
        private val mainActionModule: ViewModelModule<BaseActionViewModel.MainActionViewModel>,
        private val actionWithNavigationModule: ViewModelModule<BaseActionViewModel.ActionWithNavigationViewModel>,
    ) : DependencyContainer {

        override fun <T : ViewModel> module(clasz: Class<T>, boolean: Boolean): ViewModelModule<*> {
            return if (boolean) mainActionModule else actionWithNavigationModule
        }
    }
}
