package ua.rodev.buttontoactionapp.presentation.main

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import ua.rodev.buttontoactionapp.data.cache.SettingsConfiguration
import ua.rodev.buttontoactionapp.presentation.Communication
import ua.rodev.buttontoactionapp.presentation.NavigationStrategy
import ua.rodev.buttontoactionapp.presentation.Screen
import ua.rodev.buttontoactionapp.presentation.settings.di.SettingsModule
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val communicationFlow: Communication.Mutable<NavigationStrategy>,
    @SettingsModule.UseComposePreferences
    private val settings: SettingsConfiguration.Mutable
) : ViewModel(), Communication.Observe<NavigationStrategy> {

    fun init() = viewModelScope.launch {
        communicationFlow.map(NavigationStrategy.Replace(Screen.Action))
    }

    fun replace(screen: Screen) = viewModelScope.launch {
        communicationFlow.map(NavigationStrategy.Replace(screen))
    }

    fun isComposeUsed() = settings.read()

    override fun collect(owner: LifecycleOwner, collector: FlowCollector<NavigationStrategy>) =
        communicationFlow.collect(owner, collector)
}
