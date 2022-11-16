package ua.rodev.buttontoactionapp.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.core.Log
import ua.rodev.buttontoactionapp.core.ManageResources
import ua.rodev.buttontoactionapp.core.NetworkMonitor
import ua.rodev.buttontoactionapp.data.HandleDomainError
import ua.rodev.buttontoactionapp.domain.HandleError
import ua.rodev.buttontoactionapp.domain.ReadRawResource
import ua.rodev.buttontoactionapp.presentation.HandleUiError
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CoreModule {

    @Singleton
    @Provides
    fun provideManageResources(
        @ApplicationContext context: Context,
    ): ManageResources = ManageResources.Main(context)

    @Singleton
    @Provides
    fun provideHandleDomainError(): HandleError<Exception> = HandleDomainError()

    @Singleton
    @Provides
    fun provideHandleUiError(
        resources: ManageResources,
    ): HandleError<String> = HandleUiError(resources)

    @Singleton
    @Provides
    fun provideLog(): Log = Log.Main()

    @Singleton
    @Provides
    fun provideReadRawResource(
        @ApplicationContext context: Context,
    ): ReadRawResource = ReadRawResource.Main(context)

    @Singleton
    @Provides
    fun provideNetworkMonitor(
        @ApplicationContext context: Context,
    ): NetworkMonitor = NetworkMonitor.Main(context)
}
