package ua.rodev.buttontoactionapp.presentation.contacts

import ua.rodev.buttontoactionapp.core.Mapper

data class ContactUi(val id: Long, private val name: String, private val avatarUri: String?) :
    Mapper<ContactUi, Boolean> {

    fun <T> map(mapper: Mapper<T>): T = mapper.map(name, avatarUri)

    interface Mapper<T> {
        fun map(id: String, avatarUri: String?): T
    }

    override fun map(data: ContactUi) = data.id == id

}

