package ua.rodev.buttontoactionapp.domain

sealed class ActionsResult {

    interface ActionResultMapper<T> {
        suspend fun map(type: ActionType, errorMessage: String): T
    }

    abstract suspend fun <T> map(mapper: ActionResultMapper<T>): T

    data class Success(private val type: ActionType) : ActionsResult() {
        override suspend fun <T> map(mapper: ActionResultMapper<T>): T = mapper.map(type, "")
    }

    data class Failure(private val message: String) : ActionsResult() {
        override suspend fun <T> map(mapper: ActionResultMapper<T>): T = mapper.map(ActionType.None, message)
    }
}
