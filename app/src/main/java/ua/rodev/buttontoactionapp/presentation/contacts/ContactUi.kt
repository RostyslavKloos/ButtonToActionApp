package ua.rodev.buttontoactionapp.presentation.contacts

import ua.rodev.buttontoactionapp.core.Mapper

data class ContactUi(private val id: Long, private val name: String, private val avatarUri: String?) :
    Mapper<ContactUi, Boolean> {

    interface Mapper<T> {
        fun map(id: String, avatarUri: String?): T
    }

    interface Call {
        fun call(id: Long)
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(name, avatarUri)

    fun call(mapper: Call) = mapper.call(id)

    fun compare(id: Long) = this.id == id

    override fun map(source: ContactUi) = source.id == id

}
