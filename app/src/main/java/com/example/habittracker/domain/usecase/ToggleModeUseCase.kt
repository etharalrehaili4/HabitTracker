package com.example.habittracker.domain.usecase

import com.example.habittracker.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ToggleModeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    fun getTheme(): StateFlow<Boolean> = settingsRepository.isDarkMode

    fun setTheme(isDark: Boolean) {
        settingsRepository.setDarkMode(isDark)
    }
}