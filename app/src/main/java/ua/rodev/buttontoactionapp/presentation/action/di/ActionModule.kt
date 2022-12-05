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
import ua.rodev.buttontoactionapp.presentation.action.main.MainActionResultMapper
import ua.rodev.buttontoactionapp.presentation.action.main.MainActionModule
import ua.rodev.buttontoactionapp.presentation.action.main.MainActionViewModel
import ua.rodev.buttontoactionapp.presentation.action.withNavigation.ActionResultNavigationMapper
import ua.rodev.buttontoactionapp.presentation.action.withNavigation.ActionWithNavigationModule
import ua.rodev.buttontoactionapp.presentation.action.withNavigation.ActionWithNavigationViewModel
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ActionModule {

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class ProgressTarget

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class Snackbar

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
    @ProgressTarget
    fun provideProgressTarget(): Target.Mutable<Int> = Target.ProgressTarget()

    @Provides
    @Singleton
    fun provideActionNavigationTarget(
        navigationTarget: Target.Mutable<NavigationStrategy>,
    ): Target.Update<NavigationStrategy> = navigationTarget

    @Provides
    @Singleton
    @Snackbar
    fun provideActionSnackbar(): Target.Mutable<String> = Target.ActionSnackbarTarget()

    @Provides
    @IntentTypeMapper
    fun provideActionResultIntentTypeMapper(
        actionFlow: Target.Mutable<ActionType>,
        @Snackbar snackbarTarget: Target.Mutable<String>,
    ): ActionResult.ActionResultMapper<Unit> = MainActionResultMapper(actionFlow, snackbarTarget)

    @Provides
    @ContactTypeMapper
    fun provideActionResultContactTypeMapper(
        navigationTarget: Target.Update<NavigationStrategy>,
        actionTarget: Target.Mutable<ActionType>,
        @Snackbar snackbarTarget: Target.Mutable<String>,
    ): ActionResult.ActionResultMapper<Unit> =
        ActionResultNavigationMapper(navigationTarget, actionTarget, snackbarTarget)

    @Provides
    fun provideActionWithNavigationModule(
        dispatchers: CoroutineDispatchers,
        interactor: ActionInteractor,
        actionFlow: Target.Mutable<ActionType>,
        @ProgressTarget progressFlow: Target.Mutable<Int>,
        @ContactTypeMapper mapper: ActionResult.ActionResultMapper<Unit>,
        @Snackbar snackbarTarget: Target.Mutable<String>,
    ): ViewModelModule<ActionWithNavigationViewModel> =
        ActionWithNavigationModule(
            dispatchers, interactor, actionFlow, progressFlow, mapper, snackbarTarget
        )

    @Provides
    fun provideMainActionModule(
        dispatchers: CoroutineDispatchers,
        interactor: ActionInteractor,
        actionFlow: Target.Mutable<ActionType>,
        @ProgressTarget progressFlow: Target.Mutable<Int>,
        @IntentTypeMapper mapper: ActionResult.ActionResultMapper<Unit>,
        @Snackbar snackbarTarget: Target.Mutable<String>,
    ): ViewModelModule<MainActionViewModel> = MainActionModule(
        dispatchers, interactor, actionFlow, progressFlow, mapper, snackbarTarget
    )

    @Provides
    fun provideDependencyContainer(
        mainActionModule: ViewModelModule<MainActionViewModel>,
        actionWithNavigationModule: ViewModelModule<ActionWithNavigationViewModel>,
    ): DependencyContainer = DependencyContainer.Main(mainActionModule, actionWithNavigationModule)
}
