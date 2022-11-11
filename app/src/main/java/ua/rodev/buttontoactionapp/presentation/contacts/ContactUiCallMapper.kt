package ua.rodev.buttontoactionapp.presentation.contacts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import ua.rodev.buttontoactionapp.presentation.contacts.ContactUi

class ContactUiCallMapper(private val context: Context) : ContactUi.Call {
    override fun call(id: Long) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri: Uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,
            id.toString()
        )
        intent.data = uri
        context.startActivity(intent)
    }
}
