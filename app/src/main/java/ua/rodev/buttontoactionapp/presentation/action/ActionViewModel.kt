package ua.rodev.buttontoactionapp.presentation.action

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ua.rodev.buttontoactionapp.ActionType
import ua.rodev.buttontoactionapp.core.DispatchersList
import ua.rodev.buttontoactionapp.domain.ActionInteractor
import javax.inject.Inject

@HiltViewModel
class ActionViewModel @Inject constructor(
    private val dispatchersList: DispatchersList,
    private val interactor: ActionInteractor,
) : ViewModel() {

    private val _action = MutableSharedFlow<ActionType>()
    val action = _action.asSharedFlow()

    fun performAction() {
        viewModelScope.launch(dispatchersList.io()) {
            _action.emit(interactor.action())
        }
    }
}
