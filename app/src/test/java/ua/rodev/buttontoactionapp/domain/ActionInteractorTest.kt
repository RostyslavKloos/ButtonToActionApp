package ua.rodev.buttontoactionapp.domain

import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTimeUtils
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ua.rodev.buttontoactionapp.core.ManageResources
import ua.rodev.buttontoactionapp.data.MainCheckValidDays
import ua.rodev.buttontoactionapp.data.cloud.ActionCloud
import ua.rodev.buttontoactionapp.data.cloud.ActionCloudToDomainMapper
import ua.rodev.buttontoactionapp.data.cloud.CloudActionsList
import ua.rodev.buttontoactionapp.presentation.ActionDomainToActionResultMapper
import ua.rodev.buttontoactionapp.presentation.HandleUiError
import java.io.File
import java.time.LocalDate

class ActionInteractorTest {

    private lateinit var interactor: ActionInteractor
    private lateinit var repository: TestRepository
    private lateinit var manageResources: TestManageResources
    private lateinit var usageHistory: TestUsageHistory
    private lateinit var handleError: HandleError<String>

    @Before
    fun setUp() {
        repository = TestRepository(ActionCloudToDomainMapper())
        manageResources = TestManageResources()
        handleError = HandleUiError(manageResources)
        usageHistory = TestUsageHistory()
        interactor = ActionInteractor.Main(
            repository,
            handleError,
            manageResources,
            MainCheckValidDays(),
//            FindActionWithoutCoolDown.Main(
//                ActionDomainToActionResultMapper(),
//                usageHistory,
//                handleError
//            ),
            usageHistory,
            ActionDomainToActionResultMapper()
        )
    }

    //region errors
    @Test
    fun `Fetch action no internet connection error`() = runBlocking {
        manageResources.changeExpected("No internet connection")

        val actual = interactor.action(System.currentTimeMillis())
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

        val actual = interactor.action(System.currentTimeMillis())
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

            val actual = interactor.action(System.currentTimeMillis())
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

        val actual = interactor.action(System.currentTimeMillis())
        val expected = ActionResult.Failure("No available actions")

        assertEquals(expected, actual)
    }

    @Test
    fun `Fetch action coolDown period error`() = runBlocking {
        val currentDay = LocalDate.now().dayOfWeek
        val action = ActionDomain(ActionType.Call, true, 1, listOf(currentDay.ordinal), 3_600)
        repository.changeExpectedList(listOf(action))
        manageResources.changeExpected("${action.mapToDomainException().action} action on coolDown")

        interactor.action(DateTimeUtils.currentTimeMillis())
        DateTimeUtils.setCurrentMillisFixed(System.currentTimeMillis() + 3_000)

        val actual = interactor.action(DateTimeUtils.currentTimeMillis())
        val expected = ActionResult.Failure("call action on coolDown")
        assertEquals(expected, actual)
    }
    //endregion

    @Test
    fun `Fetch action success, high priority selected`() = runBlocking {
        val weekDays = listOf(0, 1, 2, 3, 4, 5, 6)
        val highPriorityAction = ActionDomain(ActionType.Call, true, 2, weekDays, 0)
        val lowPriorityAction = ActionDomain(ActionType.Animation, true, 1, weekDays, 0)
        repository.changeExpectedList(listOf(highPriorityAction, lowPriorityAction))

        val actual = interactor.action(System.currentTimeMillis())
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
        val action = ActionDomain(ActionType.Toast, true, 1, listOf(currentDay.ordinal), 7_200)
        usageHistory.clear()
        usageHistory.actionKey = ActionType.Toast.value
        repository.changeExpectedList(listOf(action))
        manageResources.changeExpected("${action.mapToDomainException().action} action on coolDown")

        val firstCall = interactor.action(DateTimeUtils.currentTimeMillis())
        assertEquals(ActionResult.Success(ActionType.Toast), firstCall)
        assertNotEquals(emptyMap<String, Long>(), usageHistory.read())

        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis() + 300)
        val secondCall = interactor.action(DateTimeUtils.currentTimeMillis())
        assertEquals(ActionResult.Failure("toast action on coolDown"), secondCall)

        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis() + 10_000)
        val thirdCall = interactor.action(DateTimeUtils.currentTimeMillis())
        assertEquals(ActionResult.Success(ActionType.Toast), thirdCall)

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
    fun `Fetch action success after coolDown period was finished1`() = runBlocking {
        val currentDay = LocalDate.now().dayOfWeek
        val action1 = ActionDomain(ActionType.Toast, true, 10, listOf(currentDay.ordinal), 7_200)
        val action2 = ActionDomain(ActionType.Call, true, 1, listOf(currentDay.ordinal), 3000)
        usageHistory.clear()
        usageHistory.actionKey = ActionType.Toast.value
        repository.changeExpectedList(listOf(action1, action2))
        manageResources.changeExpected("${action1.mapToDomainException().action} action on coolDown")

        val firstCall = interactor.action(DateTimeUtils.currentTimeMillis())
        assertEquals(ActionResult.Success(ActionType.Toast), firstCall)
        assertNotEquals(emptyMap<String, Long>(), usageHistory.read())

        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis() + 300)
        val secondCall = interactor.action(DateTimeUtils.currentTimeMillis())
        assertEquals(ActionResult.Success(ActionType.Call), secondCall)
        assertEquals(2, usageHistory.read().count())

        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis() + 10_000)
        val thirdCall = interactor.action(DateTimeUtils.currentTimeMillis())
        assertEquals(ActionResult.Success(ActionType.Toast), thirdCall)
        assertTrue(usageHistory.coolDownHistory.last() > usageHistory.coolDownHistory.first() + 7_200)
    }

    class TestManageResources : ManageResources {

        private var value = ""

        fun changeExpected(source: String) {
            value = source
        }

        override fun string(id: Int): String = value
        override fun string(id: Int, arg: String): String = value
    }

    class TestRepository(
        private val mapper: ActionCloud.Mapper<ActionDomain>,
        private val path: String = "/mock.json",
        private val gson: Gson = Gson(),
    ) : ActionRepository {

        private val actions = mutableListOf<ActionDomain>()
        var fetchActionsCalledCount = 0
        var errorWhileFetchingActions = false

        fun changeExpectedList(list: List<ActionDomain>) {
            actions.clear()
            actions.addAll(list)
        }

        fun initMockedList(): List<ActionDomain> {
            val resource = javaClass.getResource(path) ?: return emptyList()
            val file = File(resource.path).readText()
            val fromJson = gson.fromJson(file, CloudActionsList::class.java)
            return fromJson.map { it.map(mapper) }
        }

        override suspend fun fetchActions(): List<ActionDomain> {
            fetchActionsCalledCount++
            if (errorWhileFetchingActions) throw DomainException.NoInternetConnection
            return actions
        }
    }

    class TestUsageHistory : ActionsTimeUsageHistoryStorage.Mutable {

        private val coolDownMap: MutableMap<String, Long> = mutableMapOf()
        val coolDownHistory = mutableListOf<Long>()
        var actionKey = ""

        fun clear() {
            coolDownMap.clear()
        }

        override fun save(data: Map<String, Long>) {
            coolDownMap.clear()
            coolDownMap.putAll(data)
            data[actionKey]?.let {
                coolDownHistory.add(it)
            }
        }

        override fun read(): Map<String, Long> {
            println("map $coolDownMap")
            return coolDownMap
        }
    }
}