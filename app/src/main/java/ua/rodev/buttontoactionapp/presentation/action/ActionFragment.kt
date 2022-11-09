package ua.rodev.buttontoactionapp.presentation.action

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.presentation.action.di.ActionViewModel
import ua.rodev.buttontoactionapp.presentation.main.MyLifecycleObserver

@AndroidEntryPoint
class ActionFragment : Fragment(R.layout.fragment_action) {

    @ActionViewModel
    private val viewModel: BaseActionViewModel.MainActionViewModel by viewModels()
    lateinit var observer: MyLifecycleObserver

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view.findViewById<Button>(R.id.btnAction)
        val context = requireContext()

        observer = MyLifecycleObserver(requireActivity())
        lifecycle.addObserver(observer)

        viewModel.collect(viewLifecycleOwner) {
            when (it) {
                ActionType.Animation -> AnimationAction(button).perform()
                ActionType.Toast -> ToastAction(context).perform()
                ActionType.Call -> CallAction(observer.resultLauncher).perform()
                ActionType.Notification -> NotificationAction(context).perform()
                ActionType.None -> {}
            }
        }

        button.setOnClickListener {
            viewModel.performAction()
        }
    }
}
