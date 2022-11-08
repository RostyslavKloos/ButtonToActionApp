package ua.rodev.buttontoactionapp.presentation.action.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.core.DispatchersList
import ua.rodev.buttontoactionapp.core.Log
import ua.rodev.buttontoactionapp.domain.ActionInteractor
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.domain.ActionsResult
import ua.rodev.buttontoactionapp.presentation.Communication
import ua.rodev.buttontoactionapp.presentation.NavigationStrategy
import ua.rodev.buttontoactionapp.presentation.action.ActionResultMapper
import ua.rodev.buttontoactionapp.presentation.action.BaseActionViewModel
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ActionModule {

    @Provides
    @Singleton
    fun provideActionFlow(): Communication.Mutable<ActionType> = Communication.Main()

    @Provides
    fun provideActionNavigation(
        navigationCommunication: Communication.Mutable<NavigationStrategy>
    ): Communication.Update<NavigationStrategy> = navigationCommunication

    @Provides
    @Singleton
    fun provideActionResultMapper(
        actionFlow: Communication.Mutable<ActionType>,
        log: Log,
    ): ActionsResult.ActionResultMapper<Unit> = ActionResultMapper(actionFlow, log)


    @Provides
    @ActionViewModel
    fun provideActionViewModel(
        dispatchersList: DispatchersList,
        interactor: ActionInteractor,
        actionFlow: Communication.Mutable<ActionType>,
        mapper: ActionsResult.ActionResultMapper<Unit>
    ): BaseActionViewModel {
        return BaseActionViewModel.MainActionViewModel(
            dispatchersList, interactor, actionFlow, mapper
        )
    }
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ActionViewModel
