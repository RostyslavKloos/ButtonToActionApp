package ua.rodev.buttontoactionapp.presentation.contacts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract

class ContactUiCallMapper(private val context: Context) : ContactUi.Mapper<Unit> {
    override fun map(id: Long, name: String, avatarUri: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri: Uri = Uri.withAppendedPath(
            ContactsContract.Contacts.CONTENT_URI,
            id.toString()
        )
        intent.data = uri
        context.startActivity(intent)
    }
}
