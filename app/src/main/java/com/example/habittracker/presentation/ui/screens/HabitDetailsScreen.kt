package com.example.habittracker.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habittracker.R
import com.example.habittracker.presentation.contracts.HabitEffect
import com.example.habittracker.presentation.contracts.HabitIntent
import com.example.habittracker.presentation.vm.HabitViewModel
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailsScreen(
    habitId: String,
    viewModel: HabitViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is HabitEffect.ShowSuccess -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is HabitEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is HabitEffect.NavigateBack -> {
                    onBackClick()
                }
            }
        }
    }

    LaunchedEffect(habitId) {
        viewModel.onEvent(HabitIntent.GetHabitById(habitId))
    }

    // -- Edit Dialog Section --
    if (showEditDialog && state.selectedHabit != null) {
        EditHabitDialog(
            currentName = state.selectedHabit!!.name ?: "",
            onDismiss = { showEditDialog = false },
            onConfirm = { newName ->
                viewModel.onEvent(HabitIntent.UpdateHabit(habitId, newName))
                showEditDialog = false
            }
        )
    }

    // -- Delete Confirmation Dialog Section --
    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            habitName = state.selectedHabit?.name ?: "",
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                viewModel.onEvent(HabitIntent.DeleteHabit(habitId))
                showDeleteDialog = false
            }
        )
    }

    // -- TopBar Section --
    Scaffold(topBar = { TopAppBar(
        title = {Text(text = stringResource(R.string.habit_details))},
        navigationIcon = {
            // -- Back Button --
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
                            onClick = { viewModel.onEvent(HabitIntent.GetHabitById(habitId)) }
                        ) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
            }

            // -- Habit Not Found State --
            state.selectedHabit == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.habit_not_found),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // -- Habit Details State --
            else -> {
                val habit = state.selectedHabit!!
                val formattedDate = SimpleDateFormat(
                    "MMM dd, yyyy 'at' HH:mm",
                    Locale.getDefault()
                ).format(Date(habit.createdAt))

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(dimensionResource(R.dimen.screen_padding)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = dimensionResource(R.dimen.card_elevation_medium)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(dimensionResource(R.dimen.screen_padding_large)),
                            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
                        ) {

                            // -- Habit Name --
                            Text(
                                text = stringResource(R.string.name),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Text(
                                text = habit.name ?: stringResource(R.string.no_name),
                                style = MaterialTheme.typography.headlineMedium
                            )

                            HorizontalDivider()

                            // -- Created At --
                            Text(
                                text = stringResource(R.string.created_at),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Text(
                                text = formattedDate,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
                    ) {

                        // -- Edit Button --
                        Button(
                            onClick = { showEditDialog = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            // -- Edit Icon and Text --
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_small))
                            )

                            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))

                            Text(stringResource(R.string.edit))
                        }

                        // -- Delete Button --
                        Button(
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            // -- Delete Icon and Text --
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_small))
                            )

                            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))

                            Text(stringResource(R.string.delete))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EditHabitDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var habitName by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        // -- Edit Icon and Title --
        icon = {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null
            )
        },
        title = {
            Text(stringResource(R.string.edit_habit))
        },

        // -- Text Field for Habit Name --
        text = {
            OutlinedTextField(
                value = habitName,
                onValueChange = { habitName = it },
                label = { Text(stringResource(R.string.habit_name)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },

        // -- Confirm and Dismiss Buttons --
        confirmButton = {
            Button(
                onClick = { onConfirm(habitName) },
                enabled = habitName.isNotBlank()
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
private fun DeleteConfirmationDialog(
    habitName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,

        // -- Delete Icon and Title --
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(text = stringResource(R.string.delete_habit_title))
        },
        text = {
            Text(text = stringResource(R.string.delete_habit_desc, habitName))
        },

        // -- Confirm and Dismiss Buttons --
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}