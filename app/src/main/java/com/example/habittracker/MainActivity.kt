package com.example.habittracker

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.habittracker.data.repository.AppLanguage
import com.example.habittracker.domain.usecase.SetLanguageUseCase
import com.example.habittracker.domain.usecase.ToggleModeUseCase
import com.example.habittracker.presentation.navigation.navGraph
import com.example.habittracker.presentation.navigation.Route
import com.example.habittracker.ui.theme.HabitTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var toggleModeUseCase: ToggleModeUseCase

    @Inject
    lateinit var setLanguageUseCase: SetLanguageUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkMode by toggleModeUseCase.getTheme().collectAsState()
            val language by setLanguageUseCase.getLanguage().collectAsState()

            // Apply language configuration
            updateLocale(language)

            HabitTrackerTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Route.HabitListScreenRoute
                ) {
                    navGraph(navController)
                }
            }
        }
    }

    private fun updateLocale(language: AppLanguage) {
        val locale = Locale(language.code)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}