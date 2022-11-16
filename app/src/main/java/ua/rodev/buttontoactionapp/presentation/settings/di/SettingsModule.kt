package ua.rodev.buttontoactionapp.presentation.settings.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import ua.rodev.buttontoactionapp.core.PreferenceDataStore
import ua.rodev.buttontoactionapp.core.Read
import ua.rodev.buttontoactionapp.core.Save
import ua.rodev.buttontoactionapp.data.cache.SettingsConfiguration
import ua.rodev.buttontoactionapp.data.cache.SettingsPreferences
import javax.inject.Qualifier

@InstallIn(ViewModelComponent::class)
@Module
object SettingsModule {

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class SettingsPreferences

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class UseComposePreferences

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class UseContactsScreenPreferences

    @Provides
    @ViewModelScoped
    @SettingsPreferences
    fun provideSettingsPreferences(
        @ApplicationContext context: Context,
    ): PreferenceDataStore<Boolean> = SettingsPreferences(context)

    @Provides
    @ViewModelScoped
    @UseComposePreferences
    fun provideUseComposePreferences(
        @SettingsPreferences preferences: PreferenceDataStore<Boolean>,
    ): SettingsConfiguration.Mutable =
        SettingsConfiguration.UseComposePreferencesWrapper(preferences)

    @Provides
    @ViewModelScoped
    @UseContactsScreenPreferences
    fun provideUseContactsScreenPreferences(
        @SettingsPreferences preferences: PreferenceDataStore<Boolean>,
    ): SettingsConfiguration.Mutable =
        SettingsConfiguration.UseContactsScreenPreferencesWrapper(preferences)

    @Provides
    @ViewModelScoped
    @UseContactsScreenPreferences
    fun provideActionScreenTypeConfigurationRead(
        @UseContactsScreenPreferences preferences: SettingsConfiguration.Mutable,
    ): Read<Boolean> = preferences

    @Provides
    @ViewModelScoped
    @UseContactsScreenPreferences
    fun provideActionScreenTypeConfigurationSave(
        @UseContactsScreenPreferences preferences: SettingsConfiguration.Mutable,
    ): Save<Boolean> = preferences

}
