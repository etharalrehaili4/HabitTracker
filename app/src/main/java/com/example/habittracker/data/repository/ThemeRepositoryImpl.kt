package com.example.habittracker.data.repository

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.habittracker.domain.repository.ThemeRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
import android.content.SharedPreferences

@Singleton
class ThemeRepositoryImpl @Inject constructor(
    @ApplicationContext private val appContext: Context
) : ThemeRepository {

    companion object {
        private const val PREFS_NAME = "theme_prefs"
        private const val KEY_IS_DARK_MODE = "is_dark_mode"
    }

    private val prefs: SharedPreferences =
        appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _isDarkMode = MutableStateFlow(getSavedTheme())

    private fun getSavedTheme(): Boolean {
        return prefs.getBoolean(KEY_IS_DARK_MODE, false)
    }

    override fun setDarkMode(isDark: Boolean) {
        // Save to SharedPreferences
        prefs.edit().putBoolean(KEY_IS_DARK_MODE, isDark).apply()

        // Update StateFlow
        _isDarkMode.value = isDark

        // Apply theme
        val mode = if (isDark) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    override fun isDarkModeFlow(): StateFlow<Boolean> = _isDarkMode.asStateFlow()
}