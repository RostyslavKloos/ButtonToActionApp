package ua.rodev.buttontoactionapp.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.data.cloud.ActionService
import ua.rodev.buttontoactionapp.data.cloud.CloudDataSource
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CloudModule {

    @Provides
    @Singleton
    fun provideCloudDataSource(
        service: ActionService,
    ): CloudDataSource = CloudDataSource.Main(service)

}
