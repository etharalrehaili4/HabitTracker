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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habittracker.R
import com.example.habittracker.domain.models.AppLanguage
import com.example.habittracker.domain.models.ENGLISH_MODEL
import com.example.habittracker.domain.models.Languages
import com.example.habittracker.presentation.vm.ClearDataResult
import com.example.habittracker.presentation.vm.LanguageViewModel
import com.example.habittracker.presentation.vm.DataManagementViewModel
import com.example.habittracker.presentation.vm.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    dataVM: DataManagementViewModel = hiltViewModel(),
    languageVM: LanguageViewModel = hiltViewModel(),
    themeVM: ThemeViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {

    val toggleLanguage = languageVM.getCurrentLanguage()
    val toggleMode by themeVM.isDarkMode.collectAsState()

    val clearDataResult by dataVM.clearDataResult.collectAsState()
    var showClearDataDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val clearDataSuccessMessage = stringResource(R.string.clear_data_success)
    val clearDataErrorMessage = stringResource(R.string.clear_data_error)

    // -- Handle Clear Data Result --
    LaunchedEffect(clearDataResult) {
        when (clearDataResult) {
            is ClearDataResult.Success -> {
                snackbarHostState.showSnackbar(clearDataSuccessMessage)
                dataVM.resetClearDataResult()
            }
            is ClearDataResult.Error -> {
                snackbarHostState.showSnackbar(clearDataErrorMessage)
                dataVM.resetClearDataResult()
            }
            null -> {}
        }
    }

    // -- Clear Data Confirmation Dialog --
    if (showClearDataDialog) {
        ClearDataConfirmationDialog(
            onConfirm = {
                dataVM.clearAllData()
                showClearDataDialog = false
            },
            onDismiss = { showClearDataDialog = false }
        )
    }

    // -- TopBar Section --
    Scaffold(topBar = { TopAppBar(
        title = { Text(text = stringResource(R.string.settings)) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(dimensionResource(R.dimen.screen_padding))
                .verticalScroll(rememberScrollState())
        ) {

            // -- Appearance and Language Section --
            SectionHeader(title = stringResource(R.string.appearance_and_language))

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))

            // -- Mode Toggle --
            SettingsToggleItem(
                title = if (toggleMode) {
                    stringResource(R.string.dark_mode)
                } else {
                    stringResource(R.string.light_mode)
                },
                isChecked = toggleMode,
                onCheckedChange = {
                    themeVM.setDarkMode(it)
                }
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))

            // -- Language Toggle --
            SettingsToggleItem(
                title = if (toggleLanguage.code == Languages.AR.code) {
                    stringResource(R.string.arabic_language)
                } else {
                    stringResource(R.string.english_language)
                },
                isChecked = toggleLanguage.code == Languages.AR.code,
                onCheckedChange = { isArabic ->
                    val newLanguage = if (isArabic) {
                        AppLanguage(
                            id = Languages.AR.id,
                            code = Languages.AR.code,
                            name = Languages.AR.displayName
                        )
                    } else {
                        ENGLISH_MODEL
                    }
                    languageVM.setLanguage(newLanguage)
                }
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

            // -- Data Management Section --
            SectionHeader(title = stringResource(R.string.data))

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))

            // -- Clear Data Button --
            ClearDataButton(onClick = { showClearDataDialog = true })

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))

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
private fun SettingsToggleItem(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.spacing_small)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            // -- Toggle Title --
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        // -- Toggle Switch --
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun ClearDataButton(onClick: () -> Unit) {

    // -- Clear Data Button --
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error
        )
    ) {
        // -- Delete Icon --
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            modifier = Modifier.size(dimensionResource(R.dimen.icon_size_small))
        )
        Text(
            // -- Button Text --
            text = stringResource(R.string.clear_all_data),
            modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_small))
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
            // -- Delete Icon --
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        // -- Dialog Title and Message --
        title = {
            Text(text = stringResource(R.string.clear_data_title))
        },
        text = {
            Text(text = stringResource(R.string.clear_data_message))
        },
        // -- Confirm and Dismiss Buttons --
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
