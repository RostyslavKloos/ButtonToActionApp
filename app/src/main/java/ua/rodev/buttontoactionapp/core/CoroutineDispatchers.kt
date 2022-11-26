package ua.rodev.buttontoactionapp.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface CoroutineDispatchers {

    fun io(): CoroutineDispatcher
    fun main(): CoroutineDispatcher

    class Main : CoroutineDispatchers {
        override fun io(): CoroutineDispatcher = Dispatchers.IO
        override fun main(): CoroutineDispatcher = Dispatchers.Main
    }
}
