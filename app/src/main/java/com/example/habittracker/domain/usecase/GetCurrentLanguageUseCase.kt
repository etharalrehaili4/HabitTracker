package com.example.habittracker.domain.usecase

import com.example.habittracker.domain.models.AppLanguage
import com.example.habittracker.domain.repository.LanguageRepository
import javax.inject.Inject

class GetCurrentLanguageUseCase @Inject constructor(
    private val settingsRepository: LanguageRepository
) {
    operator fun invoke(): AppLanguage = settingsRepository.getCurrentLanguage()

}