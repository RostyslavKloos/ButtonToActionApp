package ua.rodev.buttontoactionapp.data.di

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.data.cache.ActionsTimeUsageHistoryStorage
import ua.rodev.buttontoactionapp.data.cache.CacheDataSource
import ua.rodev.buttontoactionapp.data.cache.PreferenceDataStore
import ua.rodev.buttontoactionapp.domain.ReadRawResource
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CacheModule {

    @Provides
    @Singleton
    fun provideCacheDataSource(
        @ApplicationContext context: Context,
        readRawResource: ReadRawResource,
    ): CacheDataSource = CacheDataSource.Main(readRawResource, context, Gson())


    @Provides
    @Singleton
    fun providePreferenceDataStore(
        @ApplicationContext context: Context,
    ): PreferenceDataStore = PreferenceDataStore.Base(Gson(), context)

    @Provides
    @Singleton
    fun provideActionsCoolDown(
        preferenceDataStore: PreferenceDataStore,
    ): ActionsTimeUsageHistoryStorage.Mutable = ActionsTimeUsageHistoryStorage.Main(preferenceDataStore)
}
