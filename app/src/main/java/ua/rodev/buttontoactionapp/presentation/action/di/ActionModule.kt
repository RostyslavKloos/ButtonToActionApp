package ua.rodev.buttontoactionapp.presentation.action.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.rodev.buttontoactionapp.core.CoroutineDispatchers
import ua.rodev.buttontoactionapp.core.ViewModelModule
import ua.rodev.buttontoactionapp.domain.ActionInteractor
import ua.rodev.buttontoactionapp.domain.ActionResult
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.presentation.NavigationStrategy
import ua.rodev.buttontoactionapp.presentation.Target
import ua.rodev.buttontoactionapp.presentation.action.*
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ActionModule {

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class ActionProgressTarget

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class ActionSnackbar

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class IntentTypeMapper

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class ContactTypeMapper

    @Provides
    @Singleton
    fun provideActionFlow(): Target.Mutable<ActionType> = Target.ActionTypeTarget()

    @Provides
    @Singleton
    @ActionProgressTarget
    fun provideProgressTarget(): Target.Mutable<Boolean> = Target.ProgressTarget()

    @Provides
    @Singleton
    fun provideActionNavigationTarget(
        navigationTarget: Target.Mutable<NavigationStrategy>,
    ): Target.Update<NavigationStrategy> = navigationTarget

    @Provides
    @Singleton
    @ActionSnackbar
    fun provideActionSnackbar(): Target.Mutable<String> = Target.ActionSnackbarTarget()

    @Provides
    @IntentTypeMapper
    fun provideActionResultIntentTypeMapper(
        actionFlow: Target.Mutable<ActionType>,
        @ActionSnackbar snackbarTarget: Target.Mutable<String>,
    ): ActionResult.ActionResultMapper<Unit> = ActionResultMapper(actionFlow, snackbarTarget)

    @Provides
    @ContactTypeMapper
    fun provideActionResultContactTypeMapper(
        navigationTarget: Target.Update<NavigationStrategy>,
        actionTarget: Target.Mutable<ActionType>,
        @ActionSnackbar snackbarTarget: Target.Mutable<String>,
    ): ActionResult.ActionResultMapper<Unit> =
        ActionResultNavigationMapper(navigationTarget, actionTarget, snackbarTarget)

    @Provides
    fun provideActionWithNavigationModule(
        dispatchers: CoroutineDispatchers,
        interactor: ActionInteractor,
        actionFlow: Target.Mutable<ActionType>,
        @ActionProgressTarget progressFlow: Target.Mutable<Boolean>,
        @ContactTypeMapper mapper: ActionResult.ActionResultMapper<Unit>,
        @ActionSnackbar snackbarTarget: Target.Mutable<String>,
    ): ViewModelModule<BaseActionViewModel.ActionWithNavigationViewModel> =
        ActionWithNavigationModule(
            dispatchers, interactor, actionFlow, progressFlow, mapper, snackbarTarget
        )

    @Provides
    fun provideMainActionModule(
        dispatchers: CoroutineDispatchers,
        interactor: ActionInteractor,
        actionFlow: Target.Mutable<ActionType>,
        @ActionProgressTarget progressFlow: Target.Mutable<Boolean>,
        @IntentTypeMapper mapper: ActionResult.ActionResultMapper<Unit>,
        @ActionSnackbar snackbarTarget: Target.Mutable<String>,
    ): ViewModelModule<BaseActionViewModel.MainActionViewModel> = MainActionModule(
        dispatchers, interactor, actionFlow, progressFlow, mapper, snackbarTarget
    )

    @Provides
    fun provideDependencyContainer(
        mainActionModule: ViewModelModule<BaseActionViewModel.MainActionViewModel>,
        actionWithNavigationModule: ViewModelModule<BaseActionViewModel.ActionWithNavigationViewModel>,
    ): DependencyContainer = DependencyContainer.Main(mainActionModule, actionWithNavigationModule)
}
