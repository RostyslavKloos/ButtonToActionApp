package ua.rodev.buttontoactionapp.data.cloud

import com.google.gson.Gson
import ua.rodev.buttontoactionapp.R
import ua.rodev.buttontoactionapp.data.FetchActions
import ua.rodev.buttontoactionapp.domain.ReadRawResource

interface CloudDataSource : FetchActions {

    class Main(private val service: ActionService) : CloudDataSource {
        override suspend fun fetchActions(): List<ActionCloud> = service.fetchActions()
    }

    class Test(
        private val readRawResource: ReadRawResource,
    ) : CloudDataSource {

        private val gson = Gson()

        override suspend fun fetchActions(): List<ActionCloud> {
            val json = readRawResource.read(R.raw.test_actions)
            val fromJson = gson.fromJson(json, CloudActionsList::class.java)
            return fromJson.toTypedArray().toList()
        }
    }
}
