package ua.rodev.buttontoactionapp.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ua.rodev.buttontoactionapp.core.ManageResources
import ua.rodev.buttontoactionapp.core.NetworkMonitor
import ua.rodev.buttontoactionapp.data.MainCheckValidDays
import ua.rodev.buttontoactionapp.presentation.HandleUiError
import java.time.LocalDate

class ActionInteractorTest {

    private lateinit var interactor: ActionInteractor
    private lateinit var repository: FakeRepository
    private lateinit var manageResources: FakeManageResources
    private lateinit var usageHistory: FakeUsageHistory
    private lateinit var handleError: HandleError<String>
    private lateinit var networkMonitor: FakeNetworkMonitor
    private lateinit var now: FakeNow

    @Before
    fun setUp() {
        manageResources = FakeManageResources()
        repository = FakeRepository()
        handleError = HandleUiError(manageResources)
        usageHistory = FakeUsageHistory()
        networkMonitor = FakeNetworkMonitor()
        now = FakeNow()
        interactor = ActionInteractor.Main(
            repository,
            handleError,
            MainCheckValidDays(),
            usageHistory,
            networkMonitor,
            now
        )
    }

    //region errors
    @Test
    fun `Fetch action no internet connection error`() = runBlocking {
        manageResources.changeExpected("No internet connection")

        val actual = interactor.actionResult()
        val expected = ActionResult.Failure("No internet connection")

        assertEquals(actual, expected)
        assertEquals(1, repository.fetchActionsCalledCount)
    }

    @Test
    fun `Fetch action no available actions error`() = runBlocking {
        val notEnabledActions = List(6) {
            ActionDomain(ActionType.Call, enabled = false, it, emptyList(), 0)
        }
        repository.changeExpectedList(notEnabledActions)
        manageResources.changeExpected("No available actions")

        val actual = interactor.actionResult()
        val expected = ActionResult.Failure("No available actions")

        assertEquals(actual, expected)
    }

    @Test
    fun `Fetch action no available actions error when no valid days for every action`() {
        runBlocking {
            val notEnabledActions = List(6) {
                ActionDomain(ActionType.Call, enabled = true, it, emptyList(), 0)
            }
            repository.changeExpectedList(notEnabledActions)
            manageResources.changeExpected("No available actions")

            val actual = interactor.actionResult()
            val expected = ActionResult.Failure("No available actions")

            assertEquals(actual, expected)
        }
    }

    @Test
    fun `Fetch action no valid days error`() = runBlocking {
        val yesterday = LocalDate.now().dayOfWeek - 1
        val tomorrow = LocalDate.now().dayOfWeek + 1
        val validActionDays = listOf(yesterday.ordinal, tomorrow.ordinal)
        val action = ActionDomain(ActionType.Call, true, 1, validActionDays, 0)
        repository.changeExpectedList(listOf(action))
        manageResources.changeExpected("No available actions")

        val actual = interactor.actionResult()
        val expected = ActionResult.Failure("No available actions")

        assertEquals(expected, actual)
    }

    @Test
    fun `Fetch action coolDown period error`() = runBlocking {
        now.addTime(0)
        val currentDay = LocalDate.now().dayOfWeek
        val action = ActionDomain(ActionType.Call, true, 1, listOf(currentDay.ordinal), 3_600)
        repository.changeExpectedList(listOf(action))
        manageResources.changeExpected("no available actions")

        interactor.actionResult()
        now.addTime(3_000)

        val actual = interactor.actionResult()
        val expected = ActionResult.Failure("no available actions")
        assertEquals(expected, actual)
    }
    //endregion

    @Test
    fun `Fetch toast action fail if there is no internet connection`() = runBlocking {
        val currentDay = LocalDate.now().dayOfWeek
        val action = ActionDomain(ActionType.Toast(), true, 1, listOf(currentDay.ordinal), 0)
        repository.changeExpectedList(listOf(action))
        manageResources.changeExpected("No available actions")
        networkMonitor.changeConnection(false)

        val firstCall = interactor.actionResult()
        assertEquals(ActionResult.Failure("No available actions"), firstCall)

        networkMonitor.changeConnection(true)

        val secondCall = interactor.actionResult()
        assertEquals(ActionResult.Success(ActionType.Toast()), secondCall)
    }

    @Test
    fun `Fetch lower priority action between toast and another if there is no internet connection`() = runBlocking {
        val currentDay = LocalDate.now().dayOfWeek
        val toastAction = ActionDomain(ActionType.Toast(), true, 10, listOf(currentDay.ordinal), 0)
        val otherAction = ActionDomain(ActionType.Notification, true, 1, listOf(currentDay.ordinal), 0)
        repository.changeExpectedList(listOf(toastAction, otherAction))
        networkMonitor.changeConnection(false)

        val firstCall = interactor.actionResult()
        assertEquals(ActionResult.Success(ActionType.Notification), firstCall)

        networkMonitor.changeConnection(true)

        val secondCall = interactor.actionResult()
        assertEquals(ActionResult.Success(ActionType.Toast()), secondCall)
    }

    @Test
    fun `Fetch action success, high priority selected`() = runBlocking {
        val weekDays = listOf(0, 1, 2, 3, 4, 5, 6)
        val highPriorityAction = ActionDomain(ActionType.Call, true, 2, weekDays, 0)
        val lowPriorityAction = ActionDomain(ActionType.Animation, true, 1, weekDays, 0)
        repository.changeExpectedList(listOf(highPriorityAction, lowPriorityAction))

        val actual = interactor.actionResult()
        val expected = ActionResult.Success(ActionType.Call)

        assertEquals(expected, actual)
    }

    /**
     * 1. Success first fetch
     * 2. Fetching the same action before cooldown finished. Error message shown
     * 3. Fetching the same action after cooldown finished. Success
     * 4. Check that cooldown period was finished
     */
    @Test
    fun `Fetch action success after coolDown period was finished`() = runBlocking {
        val currentDay = LocalDate.now().dayOfWeek
        val action = ActionDomain(ActionType.Toast(), true, 1, listOf(currentDay.ordinal), 7_200)
        usageHistory.clear()
        usageHistory.actionKey = ActionType.Toast().value
        repository.changeExpectedList(listOf(action))
        manageResources.changeExpected("no available actions")

        val firstCall = interactor.actionResult()
        assertEquals(ActionResult.Success(ActionType.Toast()), firstCall)
        assertNotEquals(emptyMap<String, Long>(), usageHistory.read())

        now.addTime(300)
        val secondCall = interactor.actionResult()
        assertEquals(ActionResult.Failure("no available actions"), secondCall)

        now.addTime(10_000)
        val thirdCall = interactor.actionResult()
        assertEquals(ActionResult.Success(ActionType.Toast()), thirdCall)

        assertNotEquals(usageHistory.coolDownHistory.first(), usageHistory.coolDownHistory.last())
        assertTrue(usageHistory.coolDownHistory.last() > usageHistory.coolDownHistory.first() + 7_200)
    }

    /**
     * Fetching several actions with different priority
     * 1. Fetch first action -> Success -> First action on cooldown.
     * 2. Fetch second action -> Success -> Second action on cooldown.
     * 3. Fetching action before both actions cooldown finished -> Error message shown
     * 4. Fetching action after cooldown finished -> Success
     */
    @Test
    fun `Fetching several actions with different priority`() = runBlocking {
        val currentDay = LocalDate.now().dayOfWeek
        val action1 = ActionDomain(ActionType.Toast(), true, 10, listOf(currentDay.ordinal), 7_200)
        val action2 = ActionDomain(ActionType.Call, true, 1, listOf(currentDay.ordinal), 3000)
        usageHistory.clear()
        usageHistory.actionKey = ActionType.Toast().value
        repository.changeExpectedList(listOf(action1, action2))
        manageResources.changeExpected("no available actions")

        val firstCall = interactor.actionResult()
        assertEquals(ActionResult.Success(ActionType.Toast()), firstCall)
        assertNotEquals(emptyMap<String, Long>(), usageHistory.read())

        now.addTime(300)
        val secondCall = interactor.actionResult()
        assertEquals(ActionResult.Success(ActionType.Call), secondCall)
        assertEquals(2, usageHistory.read().count())

        now.addTime(10_000)
        val thirdCall = interactor.actionResult()
        assertEquals(ActionResult.Success(ActionType.Toast()), thirdCall)
        assertTrue(usageHistory.coolDownHistory.last() > usageHistory.coolDownHistory.first() + 7_200)
    }

    private class FakeManageResources : ManageResources {

        private var value = ""

        fun changeExpected(source: String) {
            value = source
        }

        override fun string(id: Int): String = value
        override fun string(id: Int, arg: String): String = value
    }

    private class FakeRepository : ActionRepository {

        private val actions = mutableListOf<ActionDomain>()
        var fetchActionsCalledCount = 0
        var errorWhileFetchingActions = false

        fun changeExpectedList(list: List<ActionDomain>) {
            actions.clear()
            actions.addAll(list)
        }

        override suspend fun fetchActions(): List<ActionDomain> {
            fetchActionsCalledCount++
            if (errorWhileFetchingActions) throw DomainException.NoInternetConnection
            return actions
        }
    }

    private class FakeUsageHistory : ActionsUsageTimeHistoryStorage.Mutable {

        private val coolDownMap: MutableMap<String, Long> = mutableMapOf()
        val coolDownHistory = mutableListOf<Long>()
        var actionKey = ""

        override fun save(data: Map<String, Long>) {
            coolDownMap.clear()
            coolDownMap.putAll(data)
            data[actionKey]?.let {
                coolDownHistory.add(it)
            }
        }

        override fun read(): Map<String, Long> = coolDownMap

        fun clear() = coolDownMap.clear()
    }

    private class FakeNetworkMonitor: NetworkMonitor {

        private var isOnline = true

        override fun isOnlineFlow(): Flow<Boolean> = flowOf(true)

        override fun isOnline(): Boolean = isOnline

        fun changeConnection(connected: Boolean) {
            isOnline = connected
        }
    }

    private class FakeNow: Now {

        private var time: Long = 0

        fun addTime(time: Long) {
            this.time += time
        }

        override fun time(): Long = time
    }
}
