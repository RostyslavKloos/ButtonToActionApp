package ua.rodev.buttontoactionapp.presentation.contacts

import android.content.Context
import android.provider.ContactsContract
import androidx.core.database.getStringOrNull

interface LocalContactsManager {
    fun fetchLocalContacts(): List<ContactUi>
}

class LocalContactsManagerImpl(private val context: Context) : LocalContactsManager {

    private val sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
    private val queryUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

    override fun fetchLocalContacts(): List<ContactUi> {
        val contacts = ArrayList<ContactUi>()
        val cursor = context.contentResolver?.query(
            queryUri,
            null,
            null,
            null,
            sortOrder
        )
        if (cursor != null && cursor.count > 0) {
            val idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIndex =
                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val photoIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)

            while (cursor.moveToNext()) {
                val id = cursor.getString(idIndex)
                val name = cursor.getString(nameIndex)
                val number = cursor.getString(numberIndex)
                val avatarUri = cursor.getStringOrNull(photoIndex)
                addNewContactOrChangeExisting(id, name, number, avatarUri, contacts)
            }
        }
        cursor?.close()
        return contacts
    }

    private fun addNewContactOrChangeExisting(
        id: String,
        name: String,
        number: String,
        avatarUri: String?,
        contacts: ArrayList<ContactUi>,
    ) {
        val existingContact = contacts.firstOrNull { it.id == id.toLong() }
        if (existingContact == null) {
            val contact = ContactUi(
                id = id.toLong(),
                name = name,
                avatarUri = avatarUri,
            )
            contacts.add(contact)
        } else {
            contacts.remove(existingContact)
            contacts.add(ContactUi(id.toLong(), name, avatarUri))
        }
    }
}
