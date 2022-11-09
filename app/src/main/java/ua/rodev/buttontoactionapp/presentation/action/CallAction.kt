package ua.rodev.buttontoactionapp.presentation.action

import android.content.Intent
import android.provider.ContactsContract
import androidx.activity.result.ActivityResultLauncher

class CallAction(private val resultLauncher: ActivityResultLauncher<Intent>) : ActionUi {
    override fun perform() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        }
        resultLauncher.launch(intent)
    }
}
