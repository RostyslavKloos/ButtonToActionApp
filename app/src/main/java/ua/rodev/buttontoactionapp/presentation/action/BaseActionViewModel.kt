package ua.rodev.buttontoactionapp.presentation.action

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.core.CoroutineDispatchers
import ua.rodev.buttontoactionapp.domain.ActionInteractor
import ua.rodev.buttontoactionapp.domain.ActionResult
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.presentation.Target

abstract class BaseActionViewModel(
    private val dispatchers: CoroutineDispatchers,
    private val interactor: ActionInteractor,
    private val actionTarget: Target.Mutable<ActionType>,
    private val progressTarget: Target.Mutable<Int>,
    private val mapper: ActionResult.ActionResultMapper<Unit>,
    private val snackbarTarget: Target.Mutable<String>,
) : ViewModel() {

    fun collectActionType(owner: LifecycleOwner, collector: FlowCollector<ActionType>) {
        actionTarget.collect(owner, collector)
    }

    fun collectProgress(owner: LifecycleOwner, collector: FlowCollector<Int>) {
        progressTarget.collect(owner, collector)
    }

    fun collectSnackbar(owner: LifecycleOwner, collector: FlowCollector<String>) {
        snackbarTarget.collect(owner, collector)
    }

    fun obtainNotificationPermissionRequest(isGranted: Boolean) {
        val actionType = if (isGranted)
            ActionType.Notification
        else
            ActionType.Toast(R.string.notification_permission_denied)
        viewModelScope.launch {
            actionTarget.map(actionType)
        }
    }

    fun performAction() {
        viewModelScope.launch {
            progressTarget.map(View.VISIBLE)
            withContext(dispatchers.io()) {
                val action = interactor.actionResult()
                action.map(mapper)
            }
            progressTarget.map(View.GONE)
        }
    }
}
