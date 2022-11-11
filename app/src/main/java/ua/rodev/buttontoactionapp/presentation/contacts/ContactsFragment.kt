package ua.rodev.buttontoactionapp.presentation.contacts

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ua.rodev.buttontoactionapp.R

@AndroidEntryPoint
class ContactsFragment : Fragment(R.layout.fragment_contacts) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        val contactsManager = LocalContactsManager.Main(context)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val callMapper = ContactUiCallMapper(context)
        val adapter = ContactsAdapter(
            object : ClickListener {
                override fun click(item: ContactUi) = item.call(callMapper)
            }
        )
        recyclerView.adapter = adapter
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                adapter.map(contactsManager.fetchLocalContacts())
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.contacts_permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }
}
