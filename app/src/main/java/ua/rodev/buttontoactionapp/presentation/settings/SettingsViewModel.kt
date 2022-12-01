package ua.rodev.buttontoactionapp.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.rodev.buttontoactionapp.data.cache.SettingsConfiguration
import ua.rodev.buttontoactionapp.presentation.Communication
import ua.rodev.buttontoactionapp.presentation.NavigationStrategy
import ua.rodev.buttontoactionapp.presentation.Screen
import ua.rodev.buttontoactionapp.presentation.settings.di.SettingsModule
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @SettingsModule.UseComposePreferences
    private val useComposePreferences: SettingsConfiguration.Mutable,
    @SettingsModule.UseContactsScreenPreferences
    private val useContactsPreferences: SettingsConfiguration.Mutable,
    private val communicationFlow: Communication.Mutable<NavigationStrategy>
) : ViewModel() {

    fun useCompose(value: Boolean) = useComposePreferences.save(value)

    fun useContactsScreen(value: Boolean) = useContactsPreferences.save(value)

    fun isComposeUsed() = useComposePreferences.read()

    fun isContactsScreenUsed() = useContactsPreferences.read()

    fun recreateFragment() {
        viewModelScope.launch {
            if (isComposeUsed()) {
                communicationFlow.map(NavigationStrategy.Replace(Screen.SettingsCompose))
            } else {
                communicationFlow.map(NavigationStrategy.Replace(Screen.Settings))
            }
        }
    }
}
