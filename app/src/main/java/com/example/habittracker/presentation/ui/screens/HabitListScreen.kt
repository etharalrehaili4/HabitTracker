package com.example.habittracker.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habittracker.R
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.presentation.contracts.HabitIntent
import com.example.habittracker.presentation.vm.HabitViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListScreen(
    viewModel: HabitViewModel = hiltViewModel(),
    onHabitClick: (String) -> Unit = {},
    onNavigateToAddHabit: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(HabitIntent.getHabits)
    }

    // -- TopBar Section --
    Scaffold(topBar = { TopAppBar(
        title = { Text(stringResource(R.string.my_habits)) },
        actions = {
            // -- Settings Button --
            IconButton(onClick = onNavigateToSettings) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
    },
        // -- Add Habit Button --
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddHabit
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Habit"
                )
            }
        }
    ) { paddingValues ->
        when {

            // -- Loading State --
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // -- Error State --
            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(dimensionResource(R.dimen.screen_padding)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        // -- Error Message --
                        Text(
                            text = stringResource(R.string.error) + " ${state.error}",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

                        // -- Retry Button --
                        Button(
                            onClick = { viewModel.onEvent(HabitIntent.getHabits) }
                        ) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
            }

            // -- Empty State --
            state.habits.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // -- No Habits Message --
                        Text(
                            text = stringResource(R.string.no_habits_title),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))

                        // -- No Habits Description --
                        Text(
                            text = stringResource(R.string.no_habits_desc),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // -- Habit List --
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(dimensionResource(R.dimen.screen_padding)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
                ) {
                    items(
                        items = state.habits,
                        key = { habit -> habit.id }
                    ) { habit ->
                        HabitItem(
                            habit = habit,
                            onClick = { onHabitClick(habit.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HabitItem(
    habit: Habit,
    onClick: () -> Unit
) {

    val formattedDate = SimpleDateFormat(
        "MMM dd, yyyy 'at' HH:mm",
        Locale.getDefault()
    ).format(Date(habit.createdAt))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_elevation_small)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.screen_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
        ) {

            // -- Habit Name --
            Text(
                text = habit.name ?: stringResource(R.string.unnamed_habit),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // -- Habit Creation Date --
            Text(
                text = stringResource(R.string.created) + formattedDate,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}