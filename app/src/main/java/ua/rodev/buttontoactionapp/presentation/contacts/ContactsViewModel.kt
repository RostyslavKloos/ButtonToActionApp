package ua.rodev.buttontoactionapp.presentation.contacts

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.core.CoroutineDispatchers
import ua.rodev.buttontoactionapp.core.ManageResources
import ua.rodev.buttontoactionapp.presentation.Target
import ua.rodev.buttontoactionapp.presentation.contacts.di.ContactsModule
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val localContacts: LocalContacts,
    @ContactsModule.Snackbar
    private val snackbarTarget: Target.Mutable<String>,
    private val contactsTarget: Target.Mutable<List<ContactUi>>,
    private val dispatchers: CoroutineDispatchers,
    private val manageResources: ManageResources,
) : ViewModel() {

    fun obtainContactsPermissionRequest(isGranted: Boolean) {
        viewModelScope.launch(dispatchers.io()) {
            if (isGranted) {
                contactsTarget.map(localContacts.contacts())
            } else {
                snackbarTarget.map(manageResources.string(R.string.contacts_permission_denied))
            }
        }
    }

    fun collectContacts(owner: LifecycleOwner, collector: FlowCollector<List<ContactUi>>) {
        contactsTarget.collect(owner, collector)
    }

    fun collectSnackbar(owner: LifecycleOwner, collector: FlowCollector<String>) {
        snackbarTarget.collect(owner, collector)
    }
}