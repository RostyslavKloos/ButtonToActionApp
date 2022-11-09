package ua.rodev.buttontoactionapp.data

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ua.rodev.buttontoactionapp.core.ManageResources
import ua.rodev.buttontoactionapp.data.cloud.ActionCloudToDomainMapper
import ua.rodev.buttontoactionapp.domain.*
import ua.rodev.buttontoactionapp.presentation.ActionDomainToActionResultMapper

class ActionInteractorImplTest {

    private lateinit var repository: ActionRepository
    private lateinit var manageResources: ManageResources
    private lateinit var actionResultMapper: ActionsResult.ActionResultMapper<ActionType>
    private lateinit var interactor: ActionInteractor

    @Before
    fun setUp() {
        repository = ActionRepository.Test(ActionCloudToDomainMapper())
        manageResources = TestManageResources()
        actionResultMapper = TestActionResultToActionTypeMapper()
        interactor = MainActionInteractor(
            repository,
            HandleError.Ui(manageResources),
            manageResources,
            CheckValidDays.Main(),
            ActionDomainToActionResultMapper()
        )
    }

    @Test
    fun init() = runBlocking {
        val action = interactor.action()
        val result = action.map(actionResultMapper)

        assertEquals(result, ActionType.Toast)
    }

    class TestManageResources : ManageResources {

        private var value = ""

        fun changeExpected(source: String) {
            value = source
        }

        override fun string(id: Int): String = value
    }

    class TestActionResultToActionTypeMapper: ActionsResult.ActionResultMapper<ActionType> {
        override suspend fun map(type: ActionType, errorMessage: String): ActionType {
            return if (errorMessage.isEmpty()) {
                type
            } else {
                ActionType.None
            }
        }
    }
}
