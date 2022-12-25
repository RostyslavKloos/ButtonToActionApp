package ua.rodev.buttontoactionapp.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.core.NetworkMonitor
import ua.rodev.buttontoactionapp.data.MainCheckValidDays
import ua.rodev.buttontoactionapp.domain.*
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DomainModule {

    @Provides
    fun provideNow(): Now = Now.Main()

    @Singleton
    @Provides
    fun provideActionInteractor(
        repository: ActionRepository,
        handleError: HandleError<String>,
        usageHistory: ActionsUsageTimeHistoryStorage.Mutable,
        networkMonitor: NetworkMonitor,
        now: Now,
    ): ActionInteractor = ActionInteractor.Main(
        repository,
        handleError,
        MainCheckValidDays(),
        usageHistory,
        networkMonitor,
        now
    )
}
