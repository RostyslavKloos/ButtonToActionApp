package ua.rodev.buttontoactionapp.presentation.action.actions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.presentation.action.ActionUi

class ToastAction(
    private val context: Context,
    @StringRes private val messageId: Int = R.string.action_is_toast,
) : ActionUi {
    override fun perform() =
        Toast.makeText(context, context.getText(messageId), Toast.LENGTH_SHORT).show()
}
