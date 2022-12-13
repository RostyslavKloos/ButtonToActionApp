package ua.rodev.buttontoactionapp

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import dagger.hilt.android.HiltAndroidApp
import ua.rodev.buttontoactionapp.presentation.action.DependencyContainer
import ua.rodev.buttontoactionapp.presentation.action.ViewModelsFactory
import javax.inject.Inject

@HiltAndroidApp
class ButtonToActionApp : Application(), ProvideViewModel {

    private lateinit var viewModelsFactory: ViewModelsFactory

    @Inject
    lateinit var dependencyContainer: DependencyContainer

    override fun onCreate() {
        super.onCreate()
        viewModelsFactory = ViewModelsFactory(dependencyContainer)
    }

    override fun <T : ViewModel> provideViewModel(clazz: Class<T>, owner: ViewModelStoreOwner): T {
        return ViewModelProvider(owner, viewModelsFactory)[clazz]
    }
}
