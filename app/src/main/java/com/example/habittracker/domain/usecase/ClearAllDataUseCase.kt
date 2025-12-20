package com.example.habittracker.domain.usecase

import com.example.habittracker.domain.repository.SettingsRepository
import javax.inject.Inject

class ClearAllDataUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            val success = settingsRepository.clearAllData()
            if (success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to clear data"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}