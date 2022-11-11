package ua.rodev.buttontoactionapp.core

import androidx.lifecycle.ViewModel

interface ViewModelModule<T : ViewModel> {
    fun viewModel(): T
}
