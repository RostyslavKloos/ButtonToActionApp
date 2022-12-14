package ua.rodev.buttontoactionapp.presentation.contacts

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.core.viewBinding
import ua.rodev.buttontoactionapp.databinding.FragmentContactsBinding

@AndroidEntryPoint
class ContactsFragment : Fragment(R.layout.fragment_contacts) {

    private val binding by viewBinding(FragmentContactsBinding::bind)
    private val viewModel: ContactsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callMapper = ContactUiCallMapper(requireContext())
        val adapter = ContactsAdapter(
            object : ClickListener {
                override fun click(item: ContactUi) = item.map(callMapper)
            }
        )
        binding.recyclerView.adapter = adapter

        viewModel.collectContacts(viewLifecycleOwner) {
            adapter.map(it)
        }

        viewModel.collectSnackbar(viewLifecycleOwner) {
            Snackbar.make(view, it, Snackbar.LENGTH_LONG).show()
        }

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            viewModel.obtainContactsPermissionRequest(isGranted)
        }
        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }
}
