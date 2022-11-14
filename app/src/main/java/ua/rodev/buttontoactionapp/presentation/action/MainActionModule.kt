package ua.rodev.buttontoactionapp.presentation.action

import ua.rodev.buttontoactionapp.core.DispatchersList
import ua.rodev.buttontoactionapp.core.ViewModelModule
import ua.rodev.buttontoactionapp.domain.ActionInteractor
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.domain.ActionResult
import ua.rodev.buttontoactionapp.presentation.Communication
import ua.rodev.buttontoactionapp.presentation.action.di.ActionModule

class MainActionModule(
    private val dispatchersList: DispatchersList,
    private val interactor: ActionInteractor,
    private val actionFlow: Communication.Mutable<ActionType>,
    @ActionModule.IntentTypeMapper private val mapper: ActionResult.ActionResultMapper<Unit>,
) : ViewModelModule<BaseActionViewModel.MainActionViewModel> {
    override fun viewModel(): BaseActionViewModel.MainActionViewModel {
        return BaseActionViewModel.MainActionViewModel(
            dispatchersList, interactor, actionFlow, mapper
        )
    }
}
