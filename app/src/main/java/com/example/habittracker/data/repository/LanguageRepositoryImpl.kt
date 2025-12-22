package com.example.habittracker.data.repository

import android.content.Context
import com.example.habittracker.data.locale.LanguageManager
import com.example.habittracker.domain.models.AppLanguage
import com.example.habittracker.domain.models.ENGLISH_MODEL
import com.example.habittracker.domain.models.Languages
import com.example.habittracker.domain.repository.LanguageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LanguageRepositoryImpl @Inject constructor(
    @ApplicationContext private val appContext: Context
) : LanguageRepository {

    override fun setLanguage(language: AppLanguage) {
        LanguageManager.setAppLanguage(language = language.code, context = appContext)
    }

    override fun getCurrentLanguage(): AppLanguage {
        val currentLanguageTag = LanguageManager.getCurrentLanguageTag(appContext)
        return when (currentLanguageTag) {
            Languages.AR.code -> AppLanguage(
                id = Languages.AR.id,
                code = Languages.AR.code,
                name = Languages.AR.displayName
            )
            else -> ENGLISH_MODEL
        }
    }
}