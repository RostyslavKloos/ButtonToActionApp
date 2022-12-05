package ua.rodev.buttontoactionapp.presentation.contacts

data class ContactUi(
    private val id: Long,
    private val name: String,
    private val avatarUri: String,
) {

    interface Mapper<T> {
        fun map(name: String, avatarUri: String): T
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(name, avatarUri)


    interface Call {
        fun call(id: Long)
    }

    fun call(mapper: Call) = mapper.call(id)

    fun compare(id: Long) = this.id == id

    fun matches(source: ContactUi) = source.id == id
}
