package com.example.habittracker.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habittracker.presentation.contracts.HabitEffect
import com.example.habittracker.presentation.contracts.HabitIntent
import com.example.habittracker.presentation.vm.HabitViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddHabitScreen(
    viewModel: HabitViewModel = hiltViewModel(),
    onHabitAdded: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    var habitName by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle effects
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is HabitEffect.ShowSuccess -> {
                    habitName = ""
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Short
                    )
                    onHabitAdded()
                }
                is HabitEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Long
                    )
                }
                is HabitEffect.NavigateBack -> {
                    onHabitAdded()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = habitName,
                onValueChange = { habitName = it },
                label = { Text("Habit Name") },
                placeholder = { Text("Enter habit name...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.formState.nameError != null,
                supportingText = {
                    state.formState.nameError?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                enabled = !state.formState.isSubmitting
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.onEvent(HabitIntent.AddHabit(habitName))
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.formState.isSubmitting
            ) {
                if (state.formState.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(if (state.formState.isSubmitting) "Adding..." else "Add Habit")
            }
        }
    }
}