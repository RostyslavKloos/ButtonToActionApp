package ua.rodev.buttontoactionapp.presentation.action

import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import ua.rodev.buttontoactionapp.core.CoroutineDispatchers
import ua.rodev.buttontoactionapp.domain.ActionInteractor
import ua.rodev.buttontoactionapp.domain.ActionResult
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.presentation.Target

abstract class BaseActionViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    protected class FakeCoroutineDispatchers(
        private val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher(),
    ) : CoroutineDispatchers {

        init {
            Dispatchers.setMain(dispatcher)
        }

        override fun io(): CoroutineDispatcher = dispatcher
        override fun main(): CoroutineDispatcher = dispatcher
    }

    protected class FakeActionInteractor : ActionInteractor {

        private var actionResult: ActionResult = ActionResult.Success(ActionType.Call)

        fun changeExpectedResult(source: ActionResult) {
            actionResult = source
        }

        override suspend fun action(currentTimeMills: Long): ActionResult = actionResult
    }

    protected class FakeActionTarget : Target.Mutable<ActionType> {

        var count = 0
        lateinit var target: ActionType

        override suspend fun map(source: ActionType) {
            target = source
            count++
        }

        override fun collect(owner: LifecycleOwner, collector: FlowCollector<ActionType>) = Unit
    }

    protected class FakeProgressTarget : Target.Mutable<Int> {

        val progressCalledList = mutableListOf<Int>()

        override suspend fun map(source: Int) {
            progressCalledList.add(source)
        }

        override fun collect(owner: LifecycleOwner, collector: FlowCollector<Int>) = Unit
    }

    protected class FakeSnackbarTarget : Target.Mutable<String> {

        var snankbarShown = 0

        override suspend fun map(source: String) {
            snankbarShown++
        }

        override fun collect(owner: LifecycleOwner, collector: FlowCollector<String>) = Unit
    }
}