package ua.rodev.buttontoactionapp.data.di

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.data.cache.ActionsTimeHistoryUsageStore
import ua.rodev.buttontoactionapp.domain.ActionsTimeUsageHistoryStorage
import ua.rodev.buttontoactionapp.data.cache.CacheDataSource
import ua.rodev.buttontoactionapp.data.cache.MainActionsTimeUsageHistoryStorage
import ua.rodev.buttontoactionapp.domain.PreferenceDataStore
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CacheModule {

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class ActionsTimeUsageHistoryStorageQualifier

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
    ): PreferenceDataStore<Map<String, Long>> {
        return ActionsTimeHistoryUsageStore(context = context)
    }

    @Provides
    fun provideActionsTimeUsageHistoryStorage(
        @ActionsTimeUsageHistoryStorageQualifier preferences: PreferenceDataStore<Map<String, Long>>,
    ): ActionsTimeUsageHistoryStorage.Mutable = MainActionsTimeUsageHistoryStorage(preferences)

}
