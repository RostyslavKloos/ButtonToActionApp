package ua.rodev.buttontoactionapp.data

import ua.rodev.buttontoactionapp.data.cache.CacheDataSource
import ua.rodev.buttontoactionapp.data.cloud.ActionCloud
import ua.rodev.buttontoactionapp.data.cloud.CloudDataSource
import ua.rodev.buttontoactionapp.domain.ActionDomain
import ua.rodev.buttontoactionapp.domain.ActionRepository
import ua.rodev.buttontoactionapp.domain.DomainException

class MainActionRepository(
    private val cloudDataSource: CloudDataSource,
    private val cacheDataSource: CacheDataSource,
    private val mapper: ActionCloud.Mapper<ActionDomain>,
) : ActionRepository {

    @Throws(DomainException::class)
    override suspend fun fetchActions(): List<ActionDomain> {
        return try {
            val cloudData = cloudDataSource.fetchActions()
            cacheDataSource.saveActions(cloudData)
            cloudData.map { it.map(mapper) }
        } catch (e: Exception) {
            try {
                val cacheData = cacheDataSource.fetchActions()
                if (cacheData.isEmpty()) {
                    throw DomainException.ServiceUnavailable
                } else {
                    cacheData.map { it.map(mapper) }
                }
            } catch (e: Exception) {
                throw DomainException.NoInternetConnection
            }
        }
    }
}
