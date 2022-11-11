package ua.rodev.buttontoactionapp.presentation.action

import ua.rodev.buttontoactionapp.core.DispatchersList
import ua.rodev.buttontoactionapp.core.ViewModelModule
import ua.rodev.buttontoactionapp.domain.ActionInteractor
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.domain.ActionsResult
import ua.rodev.buttontoactionapp.presentation.Communication
import ua.rodev.buttontoactionapp.presentation.action.di.ActionModule

class ActionWithNavigationModule(
    private val dispatchersList: DispatchersList,
    private val interactor: ActionInteractor,
    private val actionFlow: Communication.Mutable<ActionType>,
    @ActionModule.ContactTypeMapper private val mapper: ActionsResult.ActionResultMapper<Unit>,
) : ViewModelModule<BaseActionViewModel.ActionWithNavigationViewModel> {
    override fun viewModel(): BaseActionViewModel.ActionWithNavigationViewModel {
        return BaseActionViewModel.ActionWithNavigationViewModel(
            dispatchersList, interactor, actionFlow, mapper
        )
    }
}
