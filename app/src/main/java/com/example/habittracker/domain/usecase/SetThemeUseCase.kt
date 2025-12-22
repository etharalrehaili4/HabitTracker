package com.example.habittracker.domain.usecase

import com.example.habittracker.domain.repository.ThemeRepository
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(
    private val themeRepository: ThemeRepository
) {
    operator fun invoke(isDark: Boolean) {
        themeRepository.setDarkMode(isDark)
    }
}