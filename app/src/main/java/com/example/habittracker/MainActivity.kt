package com.example.habittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.habittracker.presentation.navigation.navGraph
import com.example.habittracker.presentation.navigation.Route
import com.example.habittracker.presentation.vm.ThemeViewModel
import com.example.habittracker.presentation.ui.theme.HabitTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkMode by themeViewModel.isDarkMode.collectAsState()

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
}