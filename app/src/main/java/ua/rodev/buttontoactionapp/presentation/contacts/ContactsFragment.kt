package ua.rodev.buttontoactionapp.presentation.contacts

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.core.viewBinding
import ua.rodev.buttontoactionapp.databinding.FragmentContactsBinding

@AndroidEntryPoint
class ContactsFragment : Fragment(R.layout.fragment_contacts) {

    private val binding by viewBinding(FragmentContactsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        val callMapper = ContactUiCallMapper(context)
        val adapter = ContactsAdapter(
            object : ClickListener {
                override fun click(item: ContactUi) = item.call(callMapper)
            }
        )
        binding.recyclerView.adapter = adapter
        val localContacts by lazy { LocalContacts.Main(context) }
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                adapter.map(localContacts.contacts())
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
