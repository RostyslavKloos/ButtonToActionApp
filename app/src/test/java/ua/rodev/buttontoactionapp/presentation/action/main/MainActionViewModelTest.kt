package ua.rodev.buttontoactionapp.presentation.action.main

import android.view.View
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ua.rodev.buttontoactionapp.domain.ActionResult
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.presentation.action.BaseActionViewModelTest

class MainActionViewModelTest : BaseActionViewModelTest() {

    private lateinit var viewModel: MainActionViewModel
    private lateinit var dispatchers: FakeCoroutineDispatchers
    private lateinit var interactor: FakeActionInteractor
    private lateinit var actionTarget: FakeActionTarget
    private lateinit var progressTarget: FakeProgressTarget
    private lateinit var mapper: ActionResult.ActionResultMapper<Unit>
    private lateinit var snackbarTarget: FakeSnackbarTarget

    @Before
    fun setUp() {
        dispatchers = FakeCoroutineDispatchers()
        interactor = FakeActionInteractor()
        actionTarget = FakeActionTarget()
        progressTarget = FakeProgressTarget()
        snackbarTarget = FakeSnackbarTarget()
        mapper = MainActionResultMapper(actionTarget, snackbarTarget)
        progressTarget
        viewModel = MainActionViewModel(
            FakeCoroutineDispatchers(),
            interactor,
            actionTarget,
            progressTarget,
            mapper,
            snackbarTarget
        )
    }

    @Test
    fun `fetch action failure`() = runBlocking {
        interactor.changeExpectedResult(ActionResult.Failure("no internet connection"))

        viewModel.performAction()

        assertEquals(2, progressTarget.progressCalledList.size)
        assertEquals(View.VISIBLE, progressTarget.progressCalledList[0])
        assertEquals(View.GONE, progressTarget.progressCalledList[1])
        assertEquals(1, snackbarTarget.snankbarShown)
        assertEquals(
            ActionResult.Failure("no internet connection"),
            interactor.actionResult()
        )
    }

    @Test
    fun `fetch action success`() = runBlocking {
        interactor.changeExpectedResult(ActionResult.Success(ActionType.Call))

        viewModel.performAction()

        assertEquals(2, progressTarget.progressCalledList.size)
        assertEquals(View.VISIBLE, progressTarget.progressCalledList[0])
        assertEquals(View.GONE, progressTarget.progressCalledList[1])
        assertEquals(0, snackbarTarget.snankbarShown)
        assertEquals(
            ActionResult.Success(ActionType.Call),
            interactor.actionResult()
        )
    }

    @Test
    fun `fetch success and then failure`() = runBlocking {
        interactor.changeExpectedResult(ActionResult.Success(ActionType.Animation))

        viewModel.performAction()

        assertEquals(2, progressTarget.progressCalledList.size)
        assertEquals(View.VISIBLE, progressTarget.progressCalledList[0])
        assertEquals(View.GONE, progressTarget.progressCalledList[1])
        assertEquals(0, snackbarTarget.snankbarShown)
        assertEquals(
            ActionResult.Success(ActionType.Animation),
            interactor.actionResult()
        )

        interactor.changeExpectedResult(ActionResult.Failure("no available actions"))

        viewModel.performAction()

        assertEquals(4, progressTarget.progressCalledList.size)
        assertEquals(View.VISIBLE, progressTarget.progressCalledList[2])
        assertEquals(View.GONE, progressTarget.progressCalledList[3])
        assertEquals(1, snackbarTarget.snankbarShown)
        assertEquals(
            ActionResult.Failure("no available actions"),
            interactor.actionResult()
        )
    }
}
