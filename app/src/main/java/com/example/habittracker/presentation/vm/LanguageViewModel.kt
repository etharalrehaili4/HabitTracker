package com.example.habittracker.presentation.vm

import androidx.lifecycle.ViewModel
import com.example.habittracker.domain.models.AppLanguage
import com.example.habittracker.domain.usecase.GetCurrentLanguageUseCase
import com.example.habittracker.domain.usecase.SetLanguageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val getCurrentLanguageUseCase: GetCurrentLanguageUseCase,
    private val setLanguageUseCase: SetLanguageUseCase
) : ViewModel() {

    fun getCurrentLanguage() = getCurrentLanguageUseCase()

    fun setLanguage(language: AppLanguage) {
        setLanguageUseCase(language)
    }

}