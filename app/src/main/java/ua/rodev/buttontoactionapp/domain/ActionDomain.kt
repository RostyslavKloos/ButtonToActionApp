package ua.rodev.buttontoactionapp.domain

data class ActionDomain(
    private val type: String,
    private val enabled: Boolean,
    // TODO make private
    val priority: Int,
    private val validDays: List<Int>,
    private val coolDown: Int,
) {
    fun canBeChosen() = enabled

    interface Mapper<T> {
        fun map(type: String): T
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(type)
}