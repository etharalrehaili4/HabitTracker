package com.example.habittracker.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habittracker.R
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

    // -- TopBar Section --
    Scaffold(topBar = { TopAppBar(
        title = { Text(stringResource(R.string.add_habit)) },
        navigationIcon = {
            // -- Back Button --
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "back"
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
                .padding(dimensionResource(R.dimen.screen_padding)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // -- Habit Name Input Field --
            OutlinedTextField(
                value = habitName,
                onValueChange = { habitName = it },
                label = { Text(stringResource(R.string.habit_name)) },
                placeholder = { Text(stringResource(R.string.enter_habit)) },
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

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

            // -- Add Habit Button --
            Button(
                onClick = {
                    viewModel.onEvent(HabitIntent.AddHabit(habitName))
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.formState.isSubmitting
            ) {
                if (state.formState.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(dimensionResource(R.dimen.progress_indicator_size)),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = dimensionResource(R.dimen.progress_indicator_stroke)
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
                }
                Text(if (state.formState.isSubmitting) stringResource(R.string.adding) else stringResource(R.string.add_habit))
            }
        }
    }
}