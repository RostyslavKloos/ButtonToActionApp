package ua.rodev.buttontoactionapp.data.remote

import com.google.gson.annotations.SerializedName
import ua.rodev.buttontoactionapp.domain.ActionDomain

data class ActionRemote(
    @SerializedName("type") private val type: String,
    @SerializedName("enabled") private val enabled: Boolean,
    @SerializedName("priority") private val priority: Int,
    @SerializedName("valid_days") private val validDays: List<Int>,
    @SerializedName("cool_down") private val coolDown: Int,
) {
    // TODO: use mapper Interface instead
    fun toDomain(): ActionDomain {
        return ActionDomain(
            type = type,
            enabled = enabled,
            priority = priority,
            validDays = validDays,
            coolDown = coolDown
        )
    }
}
