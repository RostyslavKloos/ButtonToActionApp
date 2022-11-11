package ua.rodev.buttontoactionapp.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.core.viewBinding
import ua.rodev.buttontoactionapp.databinding.FragmentSettingsBinding

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val viewModel: SettingsViewModel by viewModels()
    private val binding by viewBinding(FragmentSettingsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            switchCompose.apply {
                isChecked = viewModel.isComposeUsed()
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.useCompose(isChecked)
                }
            }
            switchContactsScreen.apply {
                isChecked = viewModel.isContactsScreenUsed()
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.useContactsScreen(isChecked)
                }
            }
        }
    }
}
