package com.example.habittracker.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.data.repository.AppLanguage
import com.example.habittracker.domain.repository.SettingsRepository
import com.example.habittracker.domain.usecase.ClearAllDataUseCase
import com.example.habittracker.domain.usecase.SetLanguageUseCase
import com.example.habittracker.domain.usecase.ToggleModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val toggleModeUseCase: ToggleModeUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val clearAllDataUseCase: ClearAllDataUseCase,
    settingsRepository: SettingsRepository
) : ViewModel() {

    val isDarkMode: StateFlow<Boolean> = toggleModeUseCase.getTheme()
    val language: StateFlow<AppLanguage> = setLanguageUseCase.getLanguage()
    val isSecureStorageEnabled: StateFlow<Boolean> = settingsRepository.isSecureStorageEnabled

    private val _clearDataResult = MutableStateFlow<ClearDataResult?>(null)
    val clearDataResult: StateFlow<ClearDataResult?> = _clearDataResult.asStateFlow()

    fun toggleDarkMode(isDark: Boolean) {
        toggleModeUseCase.setTheme(isDark)
    }

    fun setLanguage(language: AppLanguage) {
        setLanguageUseCase.setLanguage(language)
    }

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