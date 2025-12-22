package com.example.habittracker.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.domain.usecase.ClearAllDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val clearAllDataUseCase: ClearAllDataUseCase,
) : ViewModel() {

    private val _clearDataResult = MutableStateFlow<ClearDataResult?>(null)
    val clearDataResult: StateFlow<ClearDataResult?> = _clearDataResult.asStateFlow()

    fun clearAllData() {
        viewModelScope.launch {
            clearAllDataUseCase()
                .onSuccess {
                    _clearDataResult.value = ClearDataResult.Success
                }
                .onFailure {
                    _clearDataResult.value = ClearDataResult.Error
                }
        }
    }

    fun resetClearDataResult() {
        _clearDataResult.value = null
    }
}

sealed class ClearDataResult {
    data object Success : ClearDataResult()
    data object Error : ClearDataResult()
}