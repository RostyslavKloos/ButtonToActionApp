package ua.rodev.buttontoactionapp.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.core.NetworkMonitor
import ua.rodev.buttontoactionapp.data.MainCheckValidDays
import ua.rodev.buttontoactionapp.domain.*
import ua.rodev.buttontoactionapp.presentation.ActionDomainToActionResultMapper
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DomainModule {

    @Singleton
    @Provides
    fun provideActionInteractor(
        repository: ActionRepository,
        handleError: HandleError<String>,
        usageHistory: ActionsUsageTimeHistoryStorage.Mutable,
        networkMonitor: NetworkMonitor
    ): ActionInteractor = ActionInteractor.Main(
        repository,
        handleError,
        MainCheckValidDays(),
        usageHistory,
        ActionDomainToActionResultMapper(),
        networkMonitor
    )
}
