package ua.rodev.buttontoactionapp.data.di

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.core.PreferenceDataStore
import ua.rodev.buttontoactionapp.data.cache.ActionsTimeHistoryPreferences
import ua.rodev.buttontoactionapp.data.cache.CacheDataSource
import ua.rodev.buttontoactionapp.data.cache.MainActionsUsageTimeHistoryStorage
import ua.rodev.buttontoactionapp.domain.ActionsUsageTimeHistoryStorage
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CacheModule {

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class ActionsUsageTimeHistoryStorageQualifier

    @Provides
    @Singleton
    fun provideCacheDataSource(
        @ApplicationContext context: Context,
    ): CacheDataSource = CacheDataSource.Main(context, Gson())

    @Provides
    @Singleton
    @ActionsUsageTimeHistoryStorageQualifier
    fun provideActionsTimeHistoryUsageStore(
        @ApplicationContext context: Context,
    ): PreferenceDataStore<Map<String, Long>> {
        return ActionsTimeHistoryPreferences(context = context)
    }

    @Provides
    fun provideActionsUsageTimeHistoryStorage(
        @ActionsUsageTimeHistoryStorageQualifier preferences: PreferenceDataStore<Map<String, Long>>,
    ): ActionsUsageTimeHistoryStorage.Mutable = MainActionsUsageTimeHistoryStorage(preferences)

}
