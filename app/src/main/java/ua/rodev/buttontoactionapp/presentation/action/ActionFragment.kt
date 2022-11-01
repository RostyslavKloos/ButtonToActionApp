package ua.rodev.buttontoactionapp.presentation.action

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ua.rodev.buttontoactionapp.ActionType
import ua.rodev.buttontoactionapp.ActionUi
import ua.rodev.buttontoactionapp.R

@AndroidEntryPoint
class ActionFragment : Fragment(R.layout.fragment_action) {

    private val viewModel: ActionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view.findViewById<Button>(R.id.btnAction)

        viewLifecycleOwner.lifecycleScope.launch {
            val context = requireContext()
            // TODO rewrite logic. collect ActionUi instead
            viewModel.action.collect {
                when (it) {
                    ActionType.Animation -> ActionUi.Animation(button).action()
                    ActionType.Toast -> ActionUi.ToastMessage(context).action()
                    ActionType.Call -> ActionUi.Call().action()
                    ActionType.Notification -> ActionUi.Notification(context).action()
                    ActionType.None -> {}
                }
            }
        }

        button.setOnClickListener {
            viewModel.performAction()
        }
    }
}
