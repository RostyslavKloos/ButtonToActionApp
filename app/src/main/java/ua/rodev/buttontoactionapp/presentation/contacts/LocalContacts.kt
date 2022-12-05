package ua.rodev.buttontoactionapp.presentation.contacts

import android.content.Context
import android.provider.ContactsContract
import androidx.core.database.getStringOrNull

interface LocalContacts {

    fun contacts(): List<ContactUi>

    class Main(private val context: Context) : LocalContacts {

        private val sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        private val queryUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

        override fun contacts(): List<ContactUi> {
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
                val photoIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)

                while (cursor.moveToNext()) {
                    val id = cursor.getString(idIndex)
                    val name = cursor.getString(nameIndex)
                    val avatarUri = cursor.getStringOrNull(photoIndex) ?: ""
                    addNewContactOrChangeExisting(id, name, avatarUri, contacts)
                }
            }
            cursor?.close()
            return contacts
        }

        private fun addNewContactOrChangeExisting(
            id: String,
            name: String,
            avatarUri: String,
            contacts: ArrayList<ContactUi>,
        ) {
            val contactId = id.toLong()
            val existingContact = contacts.firstOrNull { it.compare(contactId) }
            if (existingContact == null) {
                val contact = ContactUi(
                    id = contactId,
                    name = name,
                    avatarUri = avatarUri,
                )
                contacts.add(contact)
            } else {
                contacts.remove(existingContact)
                contacts.add(ContactUi(contactId, name, avatarUri))
            }
        }
    }
}
