package ua.rodev.buttontoactionapp.presentation.action.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.core.DispatchersList
import ua.rodev.buttontoactionapp.core.Log
import ua.rodev.buttontoactionapp.core.ViewModelModule
import ua.rodev.buttontoactionapp.domain.ActionInteractor
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.domain.ActionResult
import ua.rodev.buttontoactionapp.presentation.Communication
import ua.rodev.buttontoactionapp.presentation.NavigationStrategy
import ua.rodev.buttontoactionapp.presentation.action.*
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ActionModule {

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class ContactTypeMapper

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class IntentTypeMapper

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class ActionViewModel

    @Provides
    @Singleton
    fun provideActionFlow(): Communication.Mutable<ActionType> = Communication.Main()

    @Provides
    fun provideActionNavigation(
        navigationCommunication: Communication.Mutable<NavigationStrategy>,
    ): Communication.Update<NavigationStrategy> = navigationCommunication

    @Provides
    @IntentTypeMapper
    fun provideActionResultMapper(
        actionFlow: Communication.Mutable<ActionType>,
        log: Log,
    ): ActionResult.ActionResultMapper<Unit> = ActionResultMapper(actionFlow, log)

    @Provides
    @ContactTypeMapper
    fun provideActionResultMapper1(
        navigationFlow: Communication.Update<NavigationStrategy>,
        actionFlow: Communication.Mutable<ActionType>,
        log: Log,
    ): ActionResult.ActionResultMapper<Unit> =
        ActionResultNavigationMapper(navigationFlow, actionFlow, log)

    @Provides
    @ActionViewModel
    fun provideActionViewModel(
        dispatchersList: DispatchersList,
        interactor: ActionInteractor,
        actionFlow: Communication.Mutable<ActionType>,
        mapper: ActionResult.ActionResultMapper<Unit>,
    ): BaseActionViewModel {
        return BaseActionViewModel.MainActionViewModel(
            dispatchersList, interactor, actionFlow, mapper
        )
    }

    @Provides
    fun provideActionWithNavigationModule(
        dispatchersList: DispatchersList,
        interactor: ActionInteractor,
        actionFlow: Communication.Mutable<ActionType>,
        @ContactTypeMapper mapper: ActionResult.ActionResultMapper<Unit>,
    ): ViewModelModule<BaseActionViewModel.ActionWithNavigationViewModel> =
        ActionWithNavigationModule(
            dispatchersList, interactor, actionFlow, mapper
        )

    @Provides
    fun provideMainActionModule(
        dispatchersList: DispatchersList,
        interactor: ActionInteractor,
        actionFlow: Communication.Mutable<ActionType>,
        @IntentTypeMapper mapper: ActionResult.ActionResultMapper<Unit>,
    ): ViewModelModule<BaseActionViewModel.MainActionViewModel> = MainActionModule(
        dispatchersList, interactor, actionFlow, mapper
    )

    @Provides
    fun provideDependencyContainer(
        mainActionModule: ViewModelModule<BaseActionViewModel.MainActionViewModel>,
        actionWithNavigationModule: ViewModelModule<BaseActionViewModel.ActionWithNavigationViewModel>,
    ): DependencyContainer = DependencyContainer.Main(mainActionModule, actionWithNavigationModule)
}
