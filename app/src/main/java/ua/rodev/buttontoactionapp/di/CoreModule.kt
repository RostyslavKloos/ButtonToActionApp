package ua.rodev.buttontoactionapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.core.Log
import ua.rodev.buttontoactionapp.core.ManageResources
import ua.rodev.buttontoactionapp.data.HandleDomainError
import ua.rodev.buttontoactionapp.domain.HandleError
import ua.rodev.buttontoactionapp.domain.ReadRawResource
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
    ): HandleError<String> = HandleError.Ui(resources)

    @Singleton
    @Provides
    fun provideLog(): Log = Log.Main()

    @Singleton
    @Provides
    fun provideReadRawResource(
        @ApplicationContext context: Context,
    ): ReadRawResource = ReadRawResource.Main(context)
}
