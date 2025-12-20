package com.example.habittracker.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habittracker.presentation.contracts.HabitEffect
import com.example.habittracker.presentation.contracts.HabitIntent
import com.example.habittracker.presentation.vm.HabitViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
    viewModel: HabitViewModel = hiltViewModel(),
    onHabitAdded: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    var habitName by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is HabitEffect.ShowSuccess -> {
                    onHabitAdded()
                }
                is HabitEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                    )
                }
                is HabitEffect.NavigateBack -> {
                    onHabitAdded()
                }
            }
        }
    }

    Scaffold(topBar = { TopAppBar(
                title = { Text("Add Habit") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
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