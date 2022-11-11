package ua.rodev.buttontoactionapp.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.core.ManageResources
import ua.rodev.buttontoactionapp.data.CheckValidDays
import ua.rodev.buttontoactionapp.data.FindActionWithoutCoolDown
import ua.rodev.buttontoactionapp.data.MainActionInteractor
import ua.rodev.buttontoactionapp.data.cache.ActionsTimeUsageHistoryStorage
import ua.rodev.buttontoactionapp.data.cache.CacheDataSource
import ua.rodev.buttontoactionapp.data.cloud.ActionCloud
import ua.rodev.buttontoactionapp.domain.ActionDomain
import ua.rodev.buttontoactionapp.domain.ActionInteractor
import ua.rodev.buttontoactionapp.domain.ActionRepository
import ua.rodev.buttontoactionapp.domain.HandleError
import ua.rodev.buttontoactionapp.presentation.ActionDomainToActionResultMapper
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DomainModule {

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class MockInteractor

    @Provides
    fun provideFindCoolDown(
        usageHistory: ActionsTimeUsageHistoryStorage.Mutable,
        handleError: HandleError<String>,
    ): FindActionWithoutCoolDown =
        FindActionWithoutCoolDown.Main(
            ActionDomainToActionResultMapper(),
            usageHistory,
            handleError
        )

    @Singleton
    @Provides
    fun provideActionInteractor(
        repository: ActionRepository,
        handleError: HandleError<String>,
        manageResources: ManageResources,
        findCoolDown: FindActionWithoutCoolDown,
    ): ActionInteractor = MainActionInteractor(
        repository,
        handleError,
        manageResources,
        CheckValidDays.Main(),
        findCoolDown
    )

    @Singleton
    @Provides
    @MockInteractor
    fun provideMockInteractor(
        handleError: HandleError<String>,
        cacheDataSource: CacheDataSource,
        mapper: ActionCloud.Mapper<ActionDomain>,
        manageResources: ManageResources,
        findCoolDown: FindActionWithoutCoolDown,
    ): ActionInteractor {
        val repository = ActionRepository.Mock(cacheDataSource, mapper)
        return MainActionInteractor(
            repository,
            handleError,
            manageResources,
            CheckValidDays.Main(),
            findCoolDown
        )
    }
}
