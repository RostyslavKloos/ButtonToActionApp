package ua.rodev.buttontoactionapp.presentation.action.withNavigation

import android.view.View
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ua.rodev.buttontoactionapp.domain.ActionResult
import ua.rodev.buttontoactionapp.domain.ActionType
import ua.rodev.buttontoactionapp.presentation.NavigationStrategy
import ua.rodev.buttontoactionapp.presentation.Screen
import ua.rodev.buttontoactionapp.presentation.Target
import ua.rodev.buttontoactionapp.presentation.action.BaseActionViewModelTest
import ua.rodev.buttontoactionapp.presentation.action.main.MainActionViewModel

class ActionWithNavigationViewModelTest : BaseActionViewModelTest() {

    private lateinit var viewModel: MainActionViewModel
    private lateinit var dispatchers: FakeCoroutineDispatchers
    private lateinit var interactor: FakeActionInteractor
    private lateinit var actionTarget: FakeActionTarget
    private lateinit var navigationTarget: FakeNavigationTarget
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
        navigationTarget = FakeNavigationTarget()
        mapper = ActionResultNavigationMapper(navigationTarget, actionTarget, snackbarTarget)
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
    fun `navigate to contacts screen if Call action fetched`() = runBlocking {
        interactor.changeExpectedResult(ActionResult.Success(ActionType.Call))

        viewModel.performAction()

        assertEquals(2, progressTarget.progressCalledList.size)
        assertEquals(View.VISIBLE, progressTarget.progressCalledList[0])
        assertEquals(View.GONE, progressTarget.progressCalledList[1])
        assertEquals(
            ActionResult.Success(ActionType.Call),
            interactor.action(System.currentTimeMillis())
        )
        assertEquals(NavigationStrategy.Add(Screen.Contacts), navigationTarget.strategy)
        assertEquals(1, navigationTarget.count)
    }

    private class FakeNavigationTarget : Target.Mutable<NavigationStrategy> {

        lateinit var strategy: NavigationStrategy
        var count = 0

        override suspend fun map(source: NavigationStrategy) {
            strategy = source
            count++
        }

        override fun collect(owner: LifecycleOwner, collector: FlowCollector<NavigationStrategy>) =
            Unit
    }
}
