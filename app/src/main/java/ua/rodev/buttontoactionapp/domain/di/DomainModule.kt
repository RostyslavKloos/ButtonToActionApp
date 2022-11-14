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
import ua.rodev.buttontoactionapp.domain.ActionInteractor
import ua.rodev.buttontoactionapp.domain.ActionRepository
import ua.rodev.buttontoactionapp.domain.HandleError
import ua.rodev.buttontoactionapp.presentation.ActionDomainToActionResultMapper
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DomainModule {

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
}
