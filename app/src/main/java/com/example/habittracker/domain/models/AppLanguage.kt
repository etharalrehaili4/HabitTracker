package com.example.habittracker.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class AppLanguage(
    val id: Int,
    val code: String,
    val name: String
)

val ENGLISH_MODEL by lazy {
    AppLanguage(
        id = Languages.EN.id,
        name = Languages.EN.displayName,
        code = Languages.EN.code
    )
}

fun AppLanguage.isArabic() = code == Languages.AR.code

enum class Languages(val id: Int, val code: String, val displayName: String) {
    AR(0, "ar", "العربية"),
    EN(1, "en", "English")
}