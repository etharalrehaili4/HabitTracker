package com.example.habittracker.presentation.vm

import androidx.lifecycle.ViewModel
import com.example.habittracker.domain.usecase.GetThemeUseCase
import com.example.habittracker.domain.usecase.SetThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    getThemeUseCase: GetThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase
) : ViewModel() {

    val isDarkMode: StateFlow<Boolean> = getThemeUseCase()

    fun setDarkMode(isDark: Boolean) {
        setThemeUseCase(isDark)
    }
}