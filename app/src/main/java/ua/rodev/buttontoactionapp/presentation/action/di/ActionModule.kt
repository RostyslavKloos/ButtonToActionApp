package ua.rodev.buttontoactionapp.presentation.action.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.core.DispatchersList
import ua.rodev.buttontoactionapp.core.Log
import ua.rodev.buttontoactionapp.core.ViewModelModule
import ua.rodev.buttontoactionapp.domain.ActionInteractor
import ua.rodev.buttontoactionapp.domain.ActionResult
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.presentation.Communication
import ua.rodev.buttontoactionapp.presentation.NavigationStrategy
import ua.rodev.buttontoactionapp.presentation.action.*
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ActionModule {

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class ActionProgressFlow

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class IntentTypeMapper

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class ContactTypeMapper

    @Provides
    fun provideActionFlow(): Communication.Mutable<ActionType> = Communication.ActionTypeFlow()

    @Provides
    @Singleton
    @ActionProgressFlow
    fun provideProgressFlow(): Communication.Mutable<Boolean> = Communication.ProgressFlow()

    @Provides
    fun provideActionNavigation(
        navigationCommunication: Communication.Mutable<NavigationStrategy>,
    ): Communication.Update<NavigationStrategy> = navigationCommunication

    @Provides
    @IntentTypeMapper
    fun provideActionResultIntentTypeMapper(
        actionFlow: Communication.Mutable<ActionType>,
        log: Log,
    ): ActionResult.ActionResultMapper<Unit> = ActionResultMapper(actionFlow, log)

    @Provides
    @ContactTypeMapper
    fun provideActionResultContactTypeMapper(
        navigationFlow: Communication.Update<NavigationStrategy>,
        actionFlow: Communication.Mutable<ActionType>,
        log: Log,
    ): ActionResult.ActionResultMapper<Unit> =
        ActionResultNavigationMapper(navigationFlow, actionFlow, log)

    @Provides
    fun provideActionWithNavigationModule(
        dispatchersList: DispatchersList,
        interactor: ActionInteractor,
        actionFlow: Communication.Mutable<ActionType>,
        @ActionProgressFlow progressFlow: Communication.Mutable<Boolean>,
        @ContactTypeMapper mapper: ActionResult.ActionResultMapper<Unit>,
    ): ViewModelModule<BaseActionViewModel.ActionWithNavigationViewModel> =
        ActionWithNavigationModule(
            dispatchersList, interactor, actionFlow, progressFlow, mapper
        )

    @Provides
    fun provideMainActionModule(
        dispatchersList: DispatchersList,
        interactor: ActionInteractor,
        actionFlow: Communication.Mutable<ActionType>,
        @ActionProgressFlow progressFlow: Communication.Mutable<Boolean>,
        @IntentTypeMapper mapper: ActionResult.ActionResultMapper<Unit>,
    ): ViewModelModule<BaseActionViewModel.MainActionViewModel> = MainActionModule(
        dispatchersList, interactor, actionFlow,progressFlow, mapper
    )

    @Provides
    fun provideDependencyContainer(
        mainActionModule: ViewModelModule<BaseActionViewModel.MainActionViewModel>,
        actionWithNavigationModule: ViewModelModule<BaseActionViewModel.ActionWithNavigationViewModel>,
    ): DependencyContainer = DependencyContainer.Main(mainActionModule, actionWithNavigationModule)
}
