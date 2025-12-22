package com.example.habittracker.domain.usecase

import com.example.habittracker.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val themeRepository: ThemeRepository
) {
    operator fun invoke(): StateFlow<Boolean> {
        return themeRepository.isDarkModeFlow()
    }
}