package ua.rodev.buttontoactionapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.data.cloud.ActionCloud
import ua.rodev.buttontoactionapp.data.cloud.ActionCloudToDomainMapper
import ua.rodev.buttontoactionapp.domain.ActionDomain

@InstallIn(SingletonComponent::class)
@Module
object MapperModule {

    @Provides
    fun provideActionCloudToDomainMapper(): ActionCloud.Mapper<ActionDomain> =
        ActionCloudToDomainMapper()
}
