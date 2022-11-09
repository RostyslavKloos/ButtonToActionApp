package ua.rodev.buttontoactionapp.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ua.rodev.buttontoactionapp.core.SuspendMapper
import ua.rodev.buttontoactionapp.domain.ActionType

interface Communication {

    interface Observe<T> {
        fun collect(owner: LifecycleOwner, collector: FlowCollector<T>)
    }

    interface Update<T> : SuspendMapper.Unit<T>

    interface Mutable<T> : Observe<T>, Update<T>

    class Main(
        private val flow: MutableSharedFlow<ActionType> = MutableSharedFlow(),
    ) : Mutable<ActionType> {

        override suspend fun map(source: ActionType) = flow.emit(source)

        override fun collect(owner: LifecycleOwner, collector: FlowCollector<ActionType>) {
            owner.lifecycleScope.launch {
                flow.collect(collector)
            }
        }
    }

    class Navigation(
        private val flow: MutableSharedFlow<NavigationStrategy> = MutableSharedFlow(),
    ) : Mutable<NavigationStrategy> {

        override suspend fun map(source: NavigationStrategy) = flow.emit(source)

        override fun collect(owner: LifecycleOwner, collector: FlowCollector<NavigationStrategy>) {
            owner.lifecycleScope.launch {
                flow.collect(collector)
            }
        }
    }
}
