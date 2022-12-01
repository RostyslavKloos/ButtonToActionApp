package ua.rodev.buttontoactionapp.presentation.action.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.core.CoroutineDispatchers
import ua.rodev.buttontoactionapp.core.Log
import ua.rodev.buttontoactionapp.core.ViewModelModule
import ua.rodev.buttontoactionapp.domain.ActionInteractor
import ua.rodev.buttontoactionapp.domain.ActionResult
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.presentation.Target
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
    @Singleton
    fun provideActionFlow(): ua.rodev.buttontoactionapp.presentation.Communication.Target.Mutable<ActionType> = Target.ActionTypeTarget()

    @Provides
    @Singleton
    @ActionProgressFlow
    fun provideProgressFlow(): ua.rodev.buttontoactionapp.presentation.Communication.Target.Mutable<Boolean> = Target.ProgressTarget()

    @Provides
    @Singleton
    fun provideActionNavigation(
        navigationCommunication: ua.rodev.buttontoactionapp.presentation.Communication.Target.Mutable<NavigationStrategy>,
    ): ua.rodev.buttontoactionapp.presentation.Communication.Target.Update<NavigationStrategy> = navigationCommunication

    @Provides
    @IntentTypeMapper
    fun provideActionResultIntentTypeMapper(
        actionFlow: ua.rodev.buttontoactionapp.presentation.Communication.Target.Mutable<ActionType>,
        log: Log,
    ): ActionResult.ActionResultMapper<Unit> = ActionResultMapper(actionFlow, log)

    @Provides
    @ContactTypeMapper
    fun provideActionResultContactTypeMapper(
        navigationFlow: ua.rodev.buttontoactionapp.presentation.Communication.Target.Update<NavigationStrategy>,
        actionFlow: ua.rodev.buttontoactionapp.presentation.Communication.Target.Mutable<ActionType>,
        log: Log,
    ): ActionResult.ActionResultMapper<Unit> =
        ActionResultNavigationMapper(navigationFlow, actionFlow, log)

    @Provides
    fun provideActionWithNavigationModule(
        dispatchers: CoroutineDispatchers,
        interactor: ActionInteractor,
        actionFlow: ua.rodev.buttontoactionapp.presentation.Communication.Target.Mutable<ActionType>,
        @ActionProgressFlow progressFlow: ua.rodev.buttontoactionapp.presentation.Communication.Target.Mutable<Boolean>,
        @ContactTypeMapper mapper: ActionResult.ActionResultMapper<Unit>,
    ): ViewModelModule<BaseActionViewModel.ActionWithNavigationViewModel> =
        ActionWithNavigationModule(
            dispatchers, interactor, actionFlow, progressFlow, mapper
        )

    @Provides
    fun provideMainActionModule(
        dispatchers: CoroutineDispatchers,
        interactor: ActionInteractor,
        actionFlow: ua.rodev.buttontoactionapp.presentation.Communication.Target.Mutable<ActionType>,
        @ActionProgressFlow progressFlow: ua.rodev.buttontoactionapp.presentation.Communication.Target.Mutable<Boolean>,
        @IntentTypeMapper mapper: ActionResult.ActionResultMapper<Unit>,
    ): ViewModelModule<BaseActionViewModel.MainActionViewModel> = MainActionModule(
        dispatchers, interactor, actionFlow,progressFlow, mapper
    )

    @Provides
    fun provideDependencyContainer(
        mainActionModule: ViewModelModule<BaseActionViewModel.MainActionViewModel>,
        actionWithNavigationModule: ViewModelModule<BaseActionViewModel.ActionWithNavigationViewModel>,
    ): DependencyContainer = DependencyContainer.Main(mainActionModule, actionWithNavigationModule)
}
