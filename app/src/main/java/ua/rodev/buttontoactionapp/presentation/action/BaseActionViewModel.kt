package ua.rodev.buttontoactionapp.presentation.action

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import ua.rodev.buttontoactionapp.core.DispatchersList
import ua.rodev.buttontoactionapp.domain.ActionInteractor
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.domain.ActionsResult
import ua.rodev.buttontoactionapp.presentation.Communication
import ua.rodev.buttontoactionapp.presentation.NavigationStrategy
import ua.rodev.buttontoactionapp.presentation.Screen
import javax.inject.Inject

abstract class BaseActionViewModel(
    private val dispatchersList: DispatchersList,
    private val interactor: ActionInteractor,
    private val actionFlow: Communication.Mutable<ActionType>,
    private val mapper: ActionsResult.ActionResultMapper<Unit>
) : ViewModel(), Communication.Observe<ActionType> {

    override fun collect(owner: LifecycleOwner, collector: FlowCollector<ActionType>) {
        actionFlow.collect(owner, collector)
    }

    fun performAction() {
        viewModelScope.launch(dispatchersList.io()) {
            val action = interactor.action()
            action.map(mapper)
        }
    }

    @HiltViewModel
    class MainActionViewModel @Inject constructor(
        dispatchersList: DispatchersList,
        interactor: ActionInteractor,
        actionFlow: Communication.Mutable<ActionType>,
        mapper: ActionsResult.ActionResultMapper<Unit>
    ) : BaseActionViewModel(dispatchersList, interactor, actionFlow, mapper)

    @HiltViewModel
    class ActionWithNavigationViewModel @Inject constructor(
        dispatchersList: DispatchersList,
        interactor: ActionInteractor,
        actionFlow: Communication.Mutable<ActionType>,
        mapper: ActionsResult.ActionResultMapper<Unit>,
        private val navigationFlow: Communication.Update<NavigationStrategy>,
    ) : BaseActionViewModel(dispatchersList, interactor, actionFlow, mapper) {

       init {
           viewModelScope.launch {
               delay(3000)
               navigationFlow.map(NavigationStrategy.Add(Screen.Contacts))
           }
       }
    }
}
