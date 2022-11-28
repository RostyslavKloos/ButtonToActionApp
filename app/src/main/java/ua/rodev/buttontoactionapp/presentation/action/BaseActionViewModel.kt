package ua.rodev.buttontoactionapp.presentation.action

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTimeUtils
import ua.rodev.buttontoactionapp.core.CoroutineDispatchers
import ua.rodev.buttontoactionapp.domain.ActionInteractor
import ua.rodev.buttontoactionapp.domain.ActionResult
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.presentation.Communication
import ua.rodev.buttontoactionapp.presentation.action.di.ActionModule
import javax.inject.Inject

abstract class BaseActionViewModel(
    private val dispatchers: CoroutineDispatchers,
    private val interactor: ActionInteractor,
    private val actionFlow: Communication.Mutable<ActionType>,
    private val progressFlow: Communication.Mutable<Boolean>,
    private val mapper: ActionResult.ActionResultMapper<Unit>,
) : ViewModel(), Communication.Observe<ActionType>{

    override fun collect(owner: LifecycleOwner, collector: FlowCollector<ActionType>) {
        actionFlow.collect(owner, collector)
    }

    fun collectProgress(owner: LifecycleOwner, collector: FlowCollector<Boolean>) {
        progressFlow.collect(owner, collector)
    }

    fun performAction() {
        viewModelScope.launch {
            progressFlow.map(true)
            withContext(dispatchers.io()) {
                val action = interactor.action(DateTimeUtils.currentTimeMillis())
                action.map(mapper)
            }
            progressFlow.map(false)
        }
    }

    @HiltViewModel
    class MainActionViewModel @Inject constructor(
        dispatchers: CoroutineDispatchers,
        interactor: ActionInteractor,
        actionFlow: Communication.Mutable<ActionType>,
        @ActionModule.ActionProgressFlow progressFlow: Communication.Mutable<Boolean>,
        @ActionModule.IntentTypeMapper mapper: ActionResult.ActionResultMapper<Unit>,
    ) : BaseActionViewModel(dispatchers, interactor, actionFlow, progressFlow, mapper)

    @HiltViewModel
    class ActionWithNavigationViewModel @Inject constructor(
        dispatchers: CoroutineDispatchers,
        interactor: ActionInteractor,
        actionFlow: Communication.Mutable<ActionType>,
        @ActionModule.ActionProgressFlow progressFlow: Communication.Mutable<Boolean>,
        @ActionModule.ContactTypeMapper mapper: ActionResult.ActionResultMapper<Unit>,
    ) : BaseActionViewModel(dispatchers, interactor, actionFlow, progressFlow, mapper)
}
