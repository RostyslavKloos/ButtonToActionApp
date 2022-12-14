package ua.rodev.buttontoactionapp.presentation.main

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.data.cache.SettingsConfiguration
import ua.rodev.buttontoactionapp.presentation.NavigationStrategy
import ua.rodev.buttontoactionapp.presentation.Screen
import ua.rodev.buttontoactionapp.presentation.Target
import ua.rodev.buttontoactionapp.presentation.settings.di.SettingsModule
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val navigationTarget: Target.Mutable<NavigationStrategy>,
    @SettingsModule.UseComposePreferences
    private val useComposeSettings: SettingsConfiguration.Mutable,
) : ViewModel(), Target.Observe<NavigationStrategy> {

    override fun collect(owner: LifecycleOwner, collector: FlowCollector<NavigationStrategy>) =
        navigationTarget.collect(owner, collector)

    fun init() = viewModelScope.launch {
        navigationTarget.map(NavigationStrategy.Replace(Screen.Action))
    }

    fun navigateTo(itemId: Int): Boolean {
        viewModelScope.launch {
            val screen = when (itemId) {
                R.id.settings -> {
                    if (useComposeSettings.read()) Screen.SettingsCompose else Screen.Settings
                }
                else -> Screen.Action
            }
            navigationTarget.map(NavigationStrategy.Replace(screen))
        }
        return true
    }
}
