package ua.rodev.buttontoactionapp.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ua.rodev.buttontoactionapp.presentation.NavigationStrategy
import ua.rodev.buttontoactionapp.presentation.Screen
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    // TODO remove val and use navigation via constructor
    private val _navigation = MutableSharedFlow<NavigationStrategy>()
    val navigation = _navigation.asSharedFlow()

    fun init() {
        viewModelScope.launch {
            _navigation.emit(NavigationStrategy.Replace(Screen.Action))
        }
    }

    fun navigate(screen: Screen) {
        viewModelScope.launch {
            _navigation.emit(NavigationStrategy.Add(screen))
        }
    }
}