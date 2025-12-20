package com.example.habittracker.domain.usecase

import com.example.habittracker.data.repository.AppLanguage
import com.example.habittracker.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class SetLanguageUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    fun getLanguage(): StateFlow<AppLanguage> = settingsRepository.language

    fun setLanguage(language: AppLanguage) {
        settingsRepository.setLanguage(language)
    }
}