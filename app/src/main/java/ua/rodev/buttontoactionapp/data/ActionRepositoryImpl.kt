package ua.rodev.buttontoactionapp.data

import ua.rodev.buttontoactionapp.data.remote.ActionService
import ua.rodev.buttontoactionapp.domain.ActionDomain
import ua.rodev.buttontoactionapp.domain.ActionRepository
import javax.inject.Inject

class ActionRepositoryImpl @Inject constructor(
    private val service: ActionService,
) : ActionRepository {
    override suspend fun fetchActions(): List<ActionDomain> {
        // TODO add try catch with and throw Domain exception
        val data = service.fetchActions()
        return data.map { it.toDomain() }
    }
}