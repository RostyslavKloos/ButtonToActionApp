package ua.rodev.buttontoactionapp.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ua.rodev.buttontoactionapp.core.SuspendMapper
import ua.rodev.buttontoactionapp.domain.ActionType

interface Communication {

    interface Observe<T> {
        fun collect(owner: LifecycleOwner, collector: FlowCollector<T>)
    }

    interface Update<T> : SuspendMapper.Unit<T>

    interface Mutable<T> : Observe<T>, Update<T>

    abstract class AbstractSharedFlow<T>(
        private val flow: MutableSharedFlow<T> = MutableSharedFlow(),
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
        private val flow: MutableStateFlow<T> = MutableStateFlow(initialValue),
    ) : Mutable<T> {
        override fun collect(owner: LifecycleOwner, collector: FlowCollector<T>) {
            owner.lifecycleScope.launch {
                flow.collect(collector)
            }
        }

        override suspend fun map(source: T) = flow.emit(source)
    }

    class ActionTypeFlow : AbstractSharedFlow<ActionType>()

    class NavigationFlow : AbstractSharedFlow<NavigationStrategy>()

    class ProgressFlow : AbstractStateFlow<Boolean>(false)
}
