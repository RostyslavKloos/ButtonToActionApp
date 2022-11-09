package ua.rodev.buttontoactionapp.presentation.contacts

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ua.rodev.buttontoactionapp.R
import kotlin.Long

@AndroidEntryPoint
class ContactsFragment : Fragment(R.layout.fragment_contacts) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contactsManager = LocalContactsManagerImpl(requireContext())
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        val callMapper = object : ContactUi.Call {
            override fun call(id: Long) {
                val intent = Intent(Intent.ACTION_VIEW)
                val uri: Uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,
                    id.toString()
                )
                intent.data = uri
                startActivity(intent)
            }
        }

        val adapter = ContactsAdapter(object : ClickListener {
            override fun click(item: ContactUi) {
                item.call(callMapper)
            }
        })
        recyclerView.adapter = adapter

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                adapter.map(contactsManager.fetchLocalContacts())
            } else {
                Toast.makeText(requireContext(), "DENIED", Toast.LENGTH_SHORT).show()
            }
        }

        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)

        // todo add permission
    }
}
