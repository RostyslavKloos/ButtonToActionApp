package ua.rodev.buttontoactionapp.presentation.action.actions

import android.content.Context
import android.widget.Toast
import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.presentation.action.ActionUi
import javax.inject.Inject

class ToastAction @Inject constructor(
    private val context: Context,
    private val message: String = context.getString(R.string.action_is_toast),
) : ActionUi {
    override fun perform() = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
