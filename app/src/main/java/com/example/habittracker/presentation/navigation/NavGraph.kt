package com.example.habittracker.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.habittracker.presentation.ui.screens.AddHabitScreen
import com.example.habittracker.presentation.ui.screens.HabitDetailsScreen
import com.example.habittracker.presentation.ui.screens.HabitListScreen
import com.example.habittracker.presentation.ui.screens.SettingsScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.navGraph(navController: NavHostController) {
    composable<Route.HabitListScreenRoute> { backStackEntry ->
        HabitListScreen(
            onHabitClick = { habitId ->
                navController.navigate(Route.HabitDetailsScreenRoute(habitId))
            },
            onNavigateToAddHabit = {
                navController.navigate(Route.AddHabitScreenRoute)
            },
            onNavigateToSettings = {
                navController.navigate(Route.SettingsScreenRoute)
            }
        )
    }

    composable<Route.AddHabitScreenRoute> { backStackEntry ->
        AddHabitScreen(
            onHabitAdded = {
                navController.popBackStack()
            },
            onBackClick = {
                navController.popBackStack()
            }
        )
    }

    composable<Route.HabitDetailsScreenRoute> { backStackEntry ->
        val route: Route.HabitDetailsScreenRoute = backStackEntry.toRoute()
        HabitDetailsScreen(
            habitId = route.habitId,
            onBackClick = {
                navController.popBackStack()
            }
        )
    }

    composable<Route.SettingsScreenRoute> {
        SettingsScreen(
            onBackClick = {
                navController.popBackStack()
            }
        )
    }
}

@Serializable
sealed interface Route {
    @Serializable
    data object HabitListScreenRoute : Route

    @Serializable
    data object AddHabitScreenRoute : Route

    @Serializable
    data class HabitDetailsScreenRoute(val habitId: String) : Route

    @Serializable
    data object SettingsScreenRoute : Route
}

