package ua.rodev.buttontoactionapp.presentation.contacts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ua.rodev.buttontoactionapp.R

@AndroidEntryPoint
class ContactsFragment : Fragment(R.layout.fragment_contacts) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contactsManager = LocalContactsManagerImpl(requireContext())
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        val adapter = ContactsAdapter(object : ClickListener {
            override fun click(item: ContactUi) {
                // TODO obtain click
            }
        })
        recyclerView.adapter = adapter

        // todo add permission
        adapter.map(contactsManager.fetchLocalContacts())
    }
}
