package com.example.habittracker.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.data.network.NetworkFailure
import com.example.habittracker.data.remote.models.NetworkError
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseMviViewModel<I : UiIntent, S : UiState, E : UiEffect>(
    initialState: S
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<E>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val uiEffect: SharedFlow<E> = _uiEffect.asSharedFlow()

    abstract fun onEvent(intent: I)

    protected fun setState(reducer: S.() -> S) {
        _uiState.update { it.reducer() }
    }

    protected fun sendEffect(builder: () -> E) {
        viewModelScope.launch {
            _uiEffect.emit(builder())
        }
    }

    protected fun sendNetworkError(
        networkError: NetworkError,
        handelValidationError: Boolean = true,
    ) {
        if (!handelValidationError && networkError.hasValidationError) return
        viewModelScope.launch {
            when (networkError.networkFailure) {
                is NetworkFailure.Client,
                is NetworkFailure.Connection,
                is NetworkFailure.Server,
                is NetworkFailure.Timeout -> {
                    //No Impl
                }
                else -> {
                    //No Impl
                }
            }
        }
    }
}

// MVI Interfaces
interface UiIntent
interface UiState
interface UiEffect

