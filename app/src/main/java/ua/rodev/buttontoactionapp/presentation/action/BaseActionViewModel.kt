package ua.rodev.buttontoactionapp.presentation.action

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import ua.rodev.buttontoactionapp.core.DispatchersList
import ua.rodev.buttontoactionapp.domain.ActionInteractor
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.domain.ActionResult
import ua.rodev.buttontoactionapp.presentation.Communication
import ua.rodev.buttontoactionapp.presentation.action.di.ActionModule
import javax.inject.Inject

abstract class BaseActionViewModel(
    private val dispatchersList: DispatchersList,
    private val interactor: ActionInteractor,
    private val actionFlow: Communication.Mutable<ActionType>,
    private val mapper: ActionResult.ActionResultMapper<Unit>,
) : ViewModel(), Communication.Observe<ActionType> {

    override fun collect(owner: LifecycleOwner, collector: FlowCollector<ActionType>) {
        actionFlow.collect(owner, collector)
    }

    fun performAction() {
        viewModelScope.launch(dispatchersList.io()) {
            val action = interactor.action()
            ActionResult.Success(ActionType.Call).map(mapper)
        }
    }

    @HiltViewModel
    class MainActionViewModel @Inject constructor(
        dispatchersList: DispatchersList,
        interactor: ActionInteractor,
        actionFlow: Communication.Mutable<ActionType>,
        @ActionModule.IntentTypeMapper mapper: ActionResult.ActionResultMapper<Unit>,
    ) : BaseActionViewModel(dispatchersList, interactor, actionFlow, mapper)

    @HiltViewModel
    class ActionWithNavigationViewModel @Inject constructor(
        dispatchersList: DispatchersList,
        interactor: ActionInteractor,
        actionFlow: Communication.Mutable<ActionType>,
        @ActionModule.ContactTypeMapper mapper: ActionResult.ActionResultMapper<Unit>,
    ) : BaseActionViewModel(dispatchersList, interactor, actionFlow, mapper)
}
