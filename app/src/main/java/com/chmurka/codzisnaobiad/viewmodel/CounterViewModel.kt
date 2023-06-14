package com.chmurka.codzisnaobiad.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*

data class CounterState(
    var counterValue: Int,
)

class CounterViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CounterState(0))
    val uiState: StateFlow<CounterState> = _uiState.asStateFlow()

    fun increment() {
        _uiState.update { currentState ->
            currentState.copy(currentState.counterValue + 1)
        }
    }
}