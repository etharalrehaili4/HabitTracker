package com.example.habittracker.domain.repository

import com.example.habittracker.domain.models.AppLanguage

interface LanguageRepository {
    fun setLanguage(language: AppLanguage)
    fun getCurrentLanguage(): AppLanguage
}