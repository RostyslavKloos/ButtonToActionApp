package ua.rodev.buttontoactionapp.presentation.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.rodev.buttontoactionapp.data.cache.SettingsConfiguration
import ua.rodev.buttontoactionapp.presentation.settings.di.SettingsModule
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @SettingsModule.UseComposePreferences
    private val useComposePreferences: SettingsConfiguration.Mutable,
    @SettingsModule.UseContactsScreenPreferences
    private val useContactsPreferences: SettingsConfiguration.Mutable,
) : ViewModel() {

    fun useCompose(value: Boolean) = useComposePreferences.save(value)

    fun useContactsScreen(value: Boolean) = useContactsPreferences.save(value)

    fun isComposeUsed() = useComposePreferences.read()

    fun isContactsScreenUsed() = useContactsPreferences.read()
}
