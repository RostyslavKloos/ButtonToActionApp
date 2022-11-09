package ua.rodev.buttontoactionapp.data.cloud

import com.google.gson.annotations.SerializedName

data class ActionCloud(
    @SerializedName("type") private val type: String,
    @SerializedName("enabled") private val enabled: Boolean,
    @SerializedName("priority") private val priority: Int,
    @SerializedName("valid_days") private val validDays: List<Int>,
    @SerializedName("cool_down") private val coolDown: Long,
) {

    interface Mapper<T> {
        fun map(
            type: String,
            enabled: Boolean,
            priority: Int,
            validDays: List<Int>,
            coolDown: Long,
        ): T
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(type, enabled, priority, validDays, coolDown)
}
