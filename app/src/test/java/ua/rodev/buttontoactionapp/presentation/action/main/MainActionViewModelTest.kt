package ua.rodev.buttontoactionapp.presentation.action.main

import android.view.View
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ua.rodev.buttontoactionapp.core.CoroutineDispatchers
import ua.rodev.buttontoactionapp.domain.ActionInteractor
import ua.rodev.buttontoactionapp.domain.ActionResult
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.presentation.Target

class MainActionViewModelTest {

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
            interactor.action(System.currentTimeMillis())
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
            interactor.action(System.currentTimeMillis())
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
            interactor.action(System.currentTimeMillis())
        )

        interactor.changeExpectedResult(ActionResult.Failure("no available actions"))

        viewModel.performAction()

        assertEquals(4, progressTarget.progressCalledList.size)
        assertEquals(View.VISIBLE, progressTarget.progressCalledList[2])
        assertEquals(View.GONE, progressTarget.progressCalledList[3])
        assertEquals(1, snackbarTarget.snankbarShown)
        assertEquals(
            ActionResult.Failure("no available actions"),
            interactor.action(System.currentTimeMillis())
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private class FakeCoroutineDispatchers(
        private val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher(),
    ) : CoroutineDispatchers {

        init {
            Dispatchers.setMain(dispatcher)
        }

        override fun io(): CoroutineDispatcher = dispatcher
        override fun main(): CoroutineDispatcher = dispatcher
    }

    private class FakeActionInteractor : ActionInteractor {

        private var actionResult: ActionResult = ActionResult.Success(ActionType.Call)

        fun changeExpectedResult(source: ActionResult) {
            actionResult = source
        }

        override suspend fun action(currentTimeMills: Long): ActionResult = actionResult
    }

    private class FakeActionTarget : Target.Mutable<ActionType> {

        var count = 0
        lateinit var target: ActionType

        override suspend fun map(source: ActionType) {
            target = source
            count++
        }

        override fun collect(owner: LifecycleOwner, collector: FlowCollector<ActionType>) = Unit
    }

    private class FakeProgressTarget : Target.Mutable<Int> {

        val progressCalledList = mutableListOf<Int>()

        override suspend fun map(source: Int) {
            progressCalledList.add(source)
        }

        override fun collect(owner: LifecycleOwner, collector: FlowCollector<Int>) = Unit
    }

    private class FakeSnackbarTarget : Target.Mutable<String> {

        var snankbarShown = 0

        override suspend fun map(source: String) {
            snankbarShown++
        }

        override fun collect(owner: LifecycleOwner, collector: FlowCollector<String>) = Unit
    }
}
