package ua.rodev.buttontoactionapp.presentation.action.main

import dagger.hilt.android.lifecycle.HiltViewModel
import ua.rodev.buttontoactionapp.core.CoroutineDispatchers
import ua.rodev.buttontoactionapp.domain.ActionInteractor
import ua.rodev.buttontoactionapp.domain.ActionResult
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.presentation.Target
import ua.rodev.buttontoactionapp.presentation.action.BaseActionViewModel
import ua.rodev.buttontoactionapp.presentation.action.di.ActionModule
import javax.inject.Inject

@HiltViewModel
class MainActionViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    interactor: ActionInteractor,
    actionTarget: Target.Mutable<ActionType>,
    @ActionModule.ProgressTarget progressFlow: Target.Mutable<Int>,
    @ActionModule.IntentTypeMapper mapper: ActionResult.ActionResultMapper<Unit>,
    @ActionModule.Snackbar snackbarTarget: Target.Mutable<String>,
) : BaseActionViewModel(
    dispatchers,
    interactor,
    actionTarget,
    progressFlow,
    mapper,
    snackbarTarget
)
