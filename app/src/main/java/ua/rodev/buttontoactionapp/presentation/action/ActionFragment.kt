package ua.rodev.buttontoactionapp.presentation.action

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.core.viewBinding
import ua.rodev.buttontoactionapp.data.cache.SettingsConfiguration
import ua.rodev.buttontoactionapp.data.cache.SettingsPreferences
import ua.rodev.buttontoactionapp.databinding.FragmentActionBinding
import ua.rodev.buttontoactionapp.presentation.action.actions.AnimationAction
import ua.rodev.buttontoactionapp.presentation.action.actions.CallAction
import ua.rodev.buttontoactionapp.presentation.action.actions.NotificationAction
import ua.rodev.buttontoactionapp.presentation.action.actions.ToastAction
import ua.rodev.buttontoactionapp.presentation.main.MyLifecycleObserver
import javax.inject.Inject

@AndroidEntryPoint
class ActionFragment : Fragment(R.layout.fragment_action), HandleAction {

    private val binding by viewBinding(FragmentActionBinding::bind)
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var viewModel: BaseActionViewModel
    private lateinit var observer: MyLifecycleObserver

    @Inject
    lateinit var dependencyContainer: DependencyContainer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isContactsScreen = SettingsConfiguration.UseContactsScreenPreferencesWrapper(
            SettingsPreferences(requireContext())
        ).read()
        viewModel = ViewModelProvider(
            this@ActionFragment,
            ActionViewModelsFactory(dependencyContainer, isContactsScreen)
        )[BaseActionViewModel::class.java]
        observer = MyLifecycleObserver(requireActivity())
        lifecycle.addObserver(observer)
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted)
                showNotification()
            else
                showToast(getString(R.string.notification_permission_denied))
        }

        binding.btnAction.setOnClickListener {
            viewModel.performAction()
        }

        viewModel.collectActionType(viewLifecycleOwner) {
            it.handle(this)
        }

        viewModel.collectProgress(viewLifecycleOwner) {
            with(binding) {
                progressBar.visibility = it
                btnAction.isVisible = it != View.VISIBLE
            }
        }

        viewModel.collectSnackbar(viewLifecycleOwner) {
            Snackbar.make(view, it, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun showAnimation() = AnimationAction(binding.btnAction).perform()

    override fun showToast(message: String) = ToastAction(requireContext(), message).perform()

    override fun call() = CallAction(observer.resultLauncher).perform()

    override fun showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            NotificationAction(requireContext()).perform()
        }
    }
}
