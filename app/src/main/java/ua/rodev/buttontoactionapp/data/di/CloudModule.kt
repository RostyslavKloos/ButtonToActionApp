package ua.rodev.buttontoactionapp.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.BuildConfig
import ua.rodev.buttontoactionapp.data.cloud.ActionService
import ua.rodev.buttontoactionapp.data.cloud.CloudDataSource
import ua.rodev.buttontoactionapp.domain.ReadRawResource
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CloudModule {

    // TODO Create some "real/fake instances holder" to avoid many conditions with BUILD_TYPE

    @Provides
    @Singleton
    fun provideCloudDataSource(
        service: ActionService,
        readRawResource: ReadRawResource
    ): CloudDataSource = when (BuildConfig.BUILD_TYPE) {
        "TEST" -> CloudDataSource.Test(readRawResource)
        else -> CloudDataSource.Main(service)
    }

}
