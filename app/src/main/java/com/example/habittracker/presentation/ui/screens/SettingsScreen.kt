package com.example.habittracker.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habittracker.R
import com.example.habittracker.data.repository.AppLanguage
import com.example.habittracker.presentation.vm.ClearDataResult
import com.example.habittracker.presentation.vm.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val language by viewModel.language.collectAsState()
    val clearDataResult by viewModel.clearDataResult.collectAsState()
    val isSecureStorageEnabled by viewModel.isSecureStorageEnabled.collectAsState()
    var showClearDataDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val clearDataSuccessMessage = stringResource(R.string.clear_data_success)
    val clearDataErrorMessage = stringResource(R.string.clear_data_error)

    LaunchedEffect(clearDataResult) {
        when (clearDataResult) {
            is ClearDataResult.Success -> {
                snackbarHostState.showSnackbar(clearDataSuccessMessage)
                viewModel.resetClearDataResult()
            }
            is ClearDataResult.Error -> {
                snackbarHostState.showSnackbar(clearDataErrorMessage)
                viewModel.resetClearDataResult()
            }
            null -> {}
        }
    }

    if (showClearDataDialog) {
        ClearDataConfirmationDialog(
            onConfirm = {
                viewModel.clearAllData()
                showClearDataDialog = false
            },
            onDismiss = { showClearDataDialog = false }
        )
    }

    Scaffold(topBar = { TopAppBar(
        title = {
            Text(text = stringResource(R.string.settings))
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Security Section
            SectionHeader(title = stringResource(R.string.security))

            SecurityStatusCard(
                isSecureStorageEnabled = isSecureStorageEnabled
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Appearance Section
            SectionHeader(title = stringResource(R.string.appearance))

            Spacer(modifier = Modifier.height(8.dp))

            SettingsToggleItem(
                title = stringResource(R.string.dark_mode),
                subtitle = if (isDarkMode) {
                    stringResource(R.string.dark_mode_on)
                } else {
                    stringResource(R.string.dark_mode_off)
                },
                isChecked = isDarkMode,
                onCheckedChange = { viewModel.toggleDarkMode(it) }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Language Section
            SectionHeader(title = stringResource(R.string.language))

            Spacer(modifier = Modifier.height(8.dp))

            SettingsToggleItem(
                title = stringResource(R.string.arabic_language),
                subtitle = if (language == AppLanguage.ARABIC) {
                    stringResource(R.string.language_arabic)
                } else {
                    stringResource(R.string.language_english)
                },
                isChecked = language == AppLanguage.ARABIC,
                onCheckedChange = { isArabic ->
                    viewModel.setLanguage(
                        if (isArabic) AppLanguage.ARABIC else AppLanguage.ENGLISH
                    )
                }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Data Section
            SectionHeader(title = stringResource(R.string.data))

            Spacer(modifier = Modifier.height(8.dp))

            ClearDataButton(onClick = { showClearDataDialog = true })

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun SecurityStatusCard(isSecureStorageEnabled: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSecureStorageEnabled) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.errorContainer
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isSecureStorageEnabled) {
                    Icons.Default.Lock
                } else {
                    Icons.Default.Lock
                },
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = if (isSecureStorageEnabled) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.secure_storage),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isSecureStorageEnabled) {
                        stringResource(R.string.secure_storage_enabled)
                    } else {
                        stringResource(R.string.secure_storage_disabled)
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (isSecureStorageEnabled) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = stringResource(R.string.enabled),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun SettingsToggleItem(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun ClearDataButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error
        )
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = stringResource(R.string.clear_all_data),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun ClearDataConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(text = stringResource(R.string.clear_data_title))
        },
        text = {
            Text(text = stringResource(R.string.clear_data_message))
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(stringResource(R.string.clear_data_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
