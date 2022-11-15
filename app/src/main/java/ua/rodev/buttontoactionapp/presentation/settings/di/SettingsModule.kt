package ua.rodev.buttontoactionapp.presentation.settings.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.core.Read
import ua.rodev.buttontoactionapp.core.Save
import ua.rodev.buttontoactionapp.domain.PreferenceDataStore
import ua.rodev.buttontoactionapp.data.cache.SettingsConfiguration
import ua.rodev.buttontoactionapp.data.cache.SettingsPreferences
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
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

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class ActionScreenTypeConfigurationPrefWrapperQualifier

    @Provides
    @SettingsPreferences
    fun provideSettingsPreferences(
        @ApplicationContext context: Context,
    ): PreferenceDataStore<Boolean> = SettingsPreferences(context)

    @Provides
    @Singleton
    @UseComposePreferences
    fun provideUseComposePreferences(
        @SettingsPreferences preferences: PreferenceDataStore<Boolean>,
    ): SettingsConfiguration.Mutable =
        SettingsConfiguration.UseComposePreferencesWrapper(preferences)

    @Provides
    @Singleton
    @UseContactsScreenPreferences
    fun provideUseContactsScreenPreferences(
        @SettingsPreferences preferences: PreferenceDataStore<Boolean>,
    ): SettingsConfiguration.Mutable =
        SettingsConfiguration.UseContactsScreenPreferencesWrapper(preferences)

    @Provides
    @UseContactsScreenPreferences
    fun provideActionScreenTypeConfigurationRead(
        @UseContactsScreenPreferences preferences: SettingsConfiguration.Mutable,
    ): Read<Boolean> = preferences

    @Provides
    @UseContactsScreenPreferences
    fun provideActionScreenTypeConfigurationSave(
        @UseContactsScreenPreferences preferences: SettingsConfiguration.Mutable,
    ): Save<Boolean> = preferences

}