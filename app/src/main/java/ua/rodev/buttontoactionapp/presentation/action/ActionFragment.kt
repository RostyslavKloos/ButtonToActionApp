package ua.rodev.buttontoactionapp.presentation.action

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.data.cache.SettingsConfiguration
import ua.rodev.buttontoactionapp.data.cache.SettingsPreferences
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.presentation.main.MyLifecycleObserver
import javax.inject.Inject

@AndroidEntryPoint
class ActionFragment : Fragment(R.layout.fragment_action) {

    @Inject
    lateinit var dependencyContainer: DependencyContainer
    lateinit var viewModel: BaseActionViewModel
    lateinit var observer: MyLifecycleObserver

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        val actionBoolean =
            SettingsConfiguration.UseContactsScreenPreferencesWrapper(SettingsPreferences(context)).read()
        viewModel = ViewModelProvider(
            this@ActionFragment,
            ViewModelsFactory(dependencyContainer, actionBoolean)
        )[BaseActionViewModel::class.java]
        val button = view.findViewById<Button>(R.id.btnAction)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        observer = MyLifecycleObserver(requireActivity())
        lifecycle.addObserver(observer)

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                NotificationAction(context).perform()
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.notification_permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.collectProgress(viewLifecycleOwner) {
            Log.e("RORO", "PROGRESS $it")
            progressBar.isVisible = it
            button.isVisible = !it
        }

        viewModel.collect(viewLifecycleOwner) {
            when (it) {
                ActionType.Animation -> AnimationAction(button).perform()
                ActionType.Toast -> ToastAction(context).perform()
                ActionType.Call -> CallAction(observer.resultLauncher).perform()
                ActionType.Notification -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        NotificationAction(context).perform()
                    }
                }
                ActionType.None -> {}
            }
        }
        button.setOnClickListener {
            viewModel.performAction()
        }
    }
}
