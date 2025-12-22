package com.example.habittracker.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface ThemeRepository {
    fun setDarkMode(isDark: Boolean)
    fun isDarkModeFlow(): StateFlow<Boolean>
}