package ua.rodev.buttontoactionapp.data

import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import okio.IOException
import org.junit.Before
import org.junit.Test
import ua.rodev.buttontoactionapp.data.cache.CacheDataSource
import ua.rodev.buttontoactionapp.data.cloud.ActionCloud
import ua.rodev.buttontoactionapp.data.cloud.ActionCloudToDomainMapper
import ua.rodev.buttontoactionapp.data.cloud.CloudDataSource
import ua.rodev.buttontoactionapp.domain.ActionDomain
import ua.rodev.buttontoactionapp.domain.ActionRepository
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.domain.DomainException
import java.net.UnknownHostException

class MainActionRepositoryTest {

    private lateinit var repository: ActionRepository
    private lateinit var cacheDataSource: TestCacheDataSource
    private lateinit var cloudDataSource: TestCloudDataSource

    @Before
    fun setUp() {
        cacheDataSource = TestCacheDataSource()
        cloudDataSource = TestCloudDataSource()
        repository = MainActionRepository(
            cloudDataSource = cloudDataSource,
            cacheDataSource = cacheDataSource,
            mapper = ActionCloudToDomainMapper(),
            handleError = HandleDomainError()
        )
    }

    @Test
    fun `Fetch actions not cached success`() = runBlocking {
        cloudDataSource.changeConnection(true)
        cloudDataSource.replaceData(listOf(ActionCloud("toast", true, 1, emptyList(), 0)))
        cacheDataSource.saveActions(listOf())

        val expected = listOf(ActionDomain(ActionType.Toast, true, 1, emptyList(), 0))
        val actual = repository.fetchActions()

        assertEquals(expected, actual)
    }

    @Test(expected = DomainException.NoInternetConnection::class)
    fun `Fetch actions not cached failure`() = runBlocking {
        cloudDataSource.changeConnection(false)
        cacheDataSource.saveActions(emptyList())

        repository.fetchActions()

        return@runBlocking
    }

    @Test
    fun `Fetch actions cached success`() = runBlocking {
        cloudDataSource.changeConnection(false)
        val action = ActionCloud("toast", true, 1, listOf(), 0)
        cacheDataSource.saveActions(listOf(action))

        val expected = listOf(ActionDomain(ActionType.Toast, true, 1, listOf(), 0))
        val actual = repository.fetchActions()

        assertEquals(expected, actual)
    }

    @Test(expected = DomainException.ServiceUnavailable::class)
    fun `Fetch actions cached failure`() = runBlocking {
        cloudDataSource.changeConnection(false)
        cacheDataSource.throwException = true
        val action = ActionCloud("call", true, 1, listOf(), 0)
        cacheDataSource.saveActions(listOf(action))

        repository.fetchActions()

        return@runBlocking
    }

    class TestCacheDataSource : CacheDataSource {

        private val actions = mutableListOf<ActionCloud>()
        var fetchActionsCalled = 0
        var throwException = false

        override fun saveActions(actions: List<ActionCloud>) {
            this.actions.clear()
            this.actions.addAll(actions)
        }

        override suspend fun fetchActions(): List<ActionCloud> {
            fetchActionsCalled++
            if (throwException) throw IOException()
            return actions
        }
    }

    class TestCloudDataSource : CloudDataSource {

        private val actions = mutableListOf<ActionCloud>()
        private var internetConnectionAvailable = true
        var fetchActionsCalled = 0

        override suspend fun fetchActions(): List<ActionCloud> {
            fetchActionsCalled++
            return if (internetConnectionAvailable) actions else throw UnknownHostException()
        }

        fun changeConnection(connected: Boolean) {
            internetConnectionAvailable = connected
        }

        fun replaceData(data: List<ActionCloud>) {
            actions.clear()
            actions.addAll(data)
        }
    }
}
