package ua.rodev.buttontoactionapp.presentation

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ua.rodev.buttontoactionapp.core.SuspendMapper
import ua.rodev.buttontoactionapp.domain.ActionType

interface Target {

    interface Observe<T> {
        fun collect(owner: LifecycleOwner, collector: FlowCollector<T>)
    }

    interface Update<T> : SuspendMapper.Unit<T>

    interface Mutable<T> : Observe<T>, Update<T>

    abstract class SingleUi<T>(
        private val channel: Channel<T> = Channel(UNLIMITED),
    ) : Mutable<T> {
        override suspend fun map(source: T) = channel.send(source)

        override fun collect(owner: LifecycleOwner, collector: FlowCollector<T>) {
            owner.lifecycleScope.launchWhenStarted {
                channel.receiveAsFlow().collect(collector)
            }
        }
    }

    abstract class AbstractFlow<T>(
        private val flow: MutableSharedFlow<T>,
    ) : Mutable<T> {
        override fun collect(owner: LifecycleOwner, collector: FlowCollector<T>) {
            owner.lifecycleScope.launch {
                flow.collect(collector)
            }
        }

        override suspend fun map(source: T) = flow.emit(source)
    }

    abstract class AbstractStateFlow<T>(
        private val initialValue: T,
        flow: MutableStateFlow<T> = MutableStateFlow(initialValue),
    ) : AbstractFlow<T>(flow)

    class ActionTypeTarget : SingleUi<ActionType>()

    class NavigationTarget : SingleUi<NavigationStrategy>()

    class ActionSnackbarTarget : SingleUi<String>()

    class ProgressTarget : AbstractStateFlow<Int>(View.GONE)
}
