package ua.rodev.buttontoactionapp.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.core.ManageResources
import ua.rodev.buttontoactionapp.data.cloud.ActionCloud
import ua.rodev.buttontoactionapp.data.cloud.ActionCloudToDomainMapper
import ua.rodev.buttontoactionapp.domain.ActionDomain

@InstallIn(SingletonComponent::class)
@Module
object DataMapperModule {

    @Provides
    fun provideActionCloudToDomainMapper(
        manageResources: ManageResources
    ): ActionCloud.Mapper<ActionDomain> = ActionCloudToDomainMapper(manageResources)
}
