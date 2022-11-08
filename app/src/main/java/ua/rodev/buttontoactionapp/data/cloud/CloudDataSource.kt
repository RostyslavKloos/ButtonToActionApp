package ua.rodev.buttontoactionapp.data.cloud

import ua.rodev.buttontoactionapp.data.FetchActions

interface CloudDataSource : FetchActions {

    class Main(private val service: ActionService) : CloudDataSource {
        override suspend fun fetchActions(): List<ActionCloud> = service.fetchActions()
    }
}
