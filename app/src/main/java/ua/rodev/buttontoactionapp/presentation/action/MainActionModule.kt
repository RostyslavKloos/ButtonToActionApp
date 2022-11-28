package ua.rodev.buttontoactionapp.presentation.action

import ua.rodev.buttontoactionapp.core.CoroutineDispatchers
import ua.rodev.buttontoactionapp.core.ViewModelModule
import ua.rodev.buttontoactionapp.domain.ActionInteractor
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.domain.ActionResult
import ua.rodev.buttontoactionapp.presentation.Communication

class MainActionModule(
    private val dispatchers: CoroutineDispatchers,
    private val interactor: ActionInteractor,
    private val actionFlow: Communication.Mutable<ActionType>,
    private val progressFlow: Communication.Mutable<Boolean>,
    private val mapper: ActionResult.ActionResultMapper<Unit>,
) : ViewModelModule<BaseActionViewModel.MainActionViewModel> {
    override fun viewModel(): BaseActionViewModel.MainActionViewModel {
        return BaseActionViewModel.MainActionViewModel(
            dispatchers, interactor, actionFlow,progressFlow, mapper
        )
    }
}
