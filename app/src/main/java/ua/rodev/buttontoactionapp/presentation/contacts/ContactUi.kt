package ua.rodev.buttontoactionapp.presentation.contacts

import ua.rodev.buttontoactionapp.core.Mapper

data class ContactUi(
    private val id: Long,
    private val name: String,
    private val avatarUri: String,
) : Mapper<ContactUi, Boolean> {

    interface Mapper<T> {
        fun map(id: Long, name: String, avatarUri: String): T

        class Matches(private val id: Long) : Mapper<Boolean> {
            override fun map(id: Long, name: String, avatarUri: String): Boolean = this.id == id
        }
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(id, name, avatarUri)

    override fun map(source: ContactUi): Boolean = source.id == id

}
