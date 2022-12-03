package ua.rodev.buttontoactionapp.presentation.action

import ua.rodev.buttontoactionapp.core.CoroutineDispatchers
import ua.rodev.buttontoactionapp.core.ViewModelModule
import ua.rodev.buttontoactionapp.domain.ActionInteractor
import ua.rodev.buttontoactionapp.domain.ActionResult
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.presentation.Target

class ActionWithNavigationModule(
    private val dispatchers: CoroutineDispatchers,
    private val interactor: ActionInteractor,
    private val actionTarget: Target.Mutable<ActionType>,
    private val progressTarget: Target.Mutable<Boolean>,
    private val mapper: ActionResult.ActionResultMapper<Unit>,
    private val snackbarTarget: Target.Mutable<String>,
) : ViewModelModule<BaseActionViewModel.ActionWithNavigationViewModel> {
    override fun viewModel(): BaseActionViewModel.ActionWithNavigationViewModel {
        return BaseActionViewModel.ActionWithNavigationViewModel(
            dispatchers, interactor, actionTarget, progressTarget, mapper, snackbarTarget
        )
    }
}
