package ua.rodev.buttontoactionapp.domain

import com.google.gson.Gson
import ua.rodev.buttontoactionapp.data.cache.CacheDataSource
import ua.rodev.buttontoactionapp.data.cloud.ActionCloud
import ua.rodev.buttontoactionapp.data.cloud.MockActions
import java.io.File

interface ActionRepository {

    suspend fun fetchActions(): List<ActionDomain>

    class Test(
        private val mapper: ActionCloud.Mapper<ActionDomain>,
        private val path: String = "/storage/emulated/0/Android/data/ua.rodev.buttontoactionapp/files/mock.json",
        private val gson: Gson = Gson(),
    ) : ActionRepository {
        override suspend fun fetchActions(): List<ActionDomain> {
            val resource = javaClass.getResource(path) ?: return emptyList()
            val file = File(resource.path).readText()
            val fromJson = gson.fromJson(file, MockActions::class.java)
            return fromJson.map { it.map(mapper) }
        }
    }

    class Mock(
        private val cacheDataSource: CacheDataSource,
        private val mapper: ActionCloud.Mapper<ActionDomain>,
        ): ActionRepository {
        override suspend fun fetchActions(): List<ActionDomain> {
            return cacheDataSource.fetchActions().map { it.map(mapper) }
        }
    }
}
