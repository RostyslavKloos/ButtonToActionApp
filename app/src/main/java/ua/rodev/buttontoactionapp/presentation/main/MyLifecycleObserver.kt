package ua.rodev.buttontoactionapp.presentation.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class MyLifecycleObserver(
    private val activity: FragmentActivity,
) : DefaultLifecycleObserver {
    lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(owner: LifecycleOwner) {
        resultLauncher = activity.activityResultRegistry.register(
            "key",
            owner,
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val contactUri = result.data?.data ?: return@register
                val contactId = arrayOf(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                val cursor = activity.contentResolver.query(
                    contactUri, contactId, null, null, null, null
                )
                if (cursor?.moveToFirst()!!) {
                    val id = cursor.getString(0)
                    val intent1 = Intent(Intent.ACTION_VIEW)
                    val uri: Uri = Uri.withAppendedPath(
                        ContactsContract.Contacts.CONTENT_URI,
                        id.toString()
                    )
                    intent1.data = uri
                    activity.startActivity(intent1)
                }
                cursor.close()
            }
        }
    }
}
