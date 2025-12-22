package com.example.habittracker.domain.usecase

import com.example.habittracker.domain.repository.SettingsRepository
import javax.inject.Inject

class ClearAllDataUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        val success = settingsRepository.clearAllData()
        return if (success) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to clear data"))
        }
    }
}