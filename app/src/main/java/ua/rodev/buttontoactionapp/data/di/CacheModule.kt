package ua.rodev.buttontoactionapp.data.di

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.core.Read
import ua.rodev.buttontoactionapp.data.cache.ActionScreenTypeConfiguration
import ua.rodev.buttontoactionapp.data.cache.ActionsTimeUsageHistoryStorage
import ua.rodev.buttontoactionapp.data.cache.CacheDataSource
import ua.rodev.buttontoactionapp.data.cache.PreferenceDataStore
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CacheModule {

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class ActionScreenTypeConfigurationQualifier

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class ActionsTimeUsageHistoryStorageQualifier

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class ActionScreenTypeConfigurationReadQualifier

    @Provides
    @Singleton
    fun provideCacheDataSource(
        @ApplicationContext context: Context,
    ): CacheDataSource = CacheDataSource.Main(context, Gson())

    @Provides
    @Singleton
    @ActionsTimeUsageHistoryStorageQualifier
    fun provideActionsTimeHistoryUsageStore(
        @ApplicationContext context: Context,
    ): PreferenceDataStore<HashMap<String, Long>> {
        return PreferenceDataStore.ActionsTimeHistoryUsageStore(context = context)
    }

    @Provides
    fun provideActionsTimeUsageHistoryStorage(
        @ActionsTimeUsageHistoryStorageQualifier preferences: PreferenceDataStore<HashMap<String, Long>>,
    ): ActionsTimeUsageHistoryStorage.Mutable = ActionsTimeUsageHistoryStorage.Main(preferences)

    @Provides
    @Singleton
    @ActionScreenTypeConfigurationQualifier
    fun provideActionScreenTypeConfigurationPreferences(
        @ApplicationContext context: Context,
    ): PreferenceDataStore<Boolean> = PreferenceDataStore.ActionScreenTypeConfiguration(context)

    @Provides
    @Singleton
    fun provideActionScreenTypeConfiguration(
        @ActionScreenTypeConfigurationQualifier preferences: PreferenceDataStore<Boolean>,
    ): ActionScreenTypeConfiguration.Mutable = ActionScreenTypeConfiguration.Main(preferences)

    @Provides
    @ActionScreenTypeConfigurationReadQualifier
    fun provideActionScreenTypeConfigurationRead(
        preferences: ActionScreenTypeConfiguration.Mutable,
    ): Read<Boolean> = preferences
}
