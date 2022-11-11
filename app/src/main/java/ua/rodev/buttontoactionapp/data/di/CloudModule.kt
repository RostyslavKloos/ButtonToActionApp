package ua.rodev.buttontoactionapp.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.data.cloud.ActionCloud
import ua.rodev.buttontoactionapp.data.cloud.ActionService
import ua.rodev.buttontoactionapp.data.cloud.CloudDataSource
import ua.rodev.buttontoactionapp.domain.ActionDomain
import ua.rodev.buttontoactionapp.domain.ActionRepository
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CloudModule {

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class MockRepo

    @Provides
    @Singleton
    fun provideCloudDataSource(
        service: ActionService,
    ): CloudDataSource = CloudDataSource.Main(service)

    @Provides
    @Singleton
    @MockRepo
    fun provideTestCaheRepo(
        mapper: ActionCloud.Mapper<ActionDomain>,
    ): ActionRepository = ActionRepository.Test(mapper)
}
