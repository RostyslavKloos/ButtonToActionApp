package ua.rodev.buttontoactionapp.presentation.action.actions

import android.content.Context
import android.widget.Toast
import ua.rodev.buttontoactionapp.presentation.action.ActionUi
import javax.inject.Inject

class ToastAction @Inject constructor(
    private val context: Context,
    private val message: String = "Action is Toast!",
) : ActionUi {
    override fun perform() = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}