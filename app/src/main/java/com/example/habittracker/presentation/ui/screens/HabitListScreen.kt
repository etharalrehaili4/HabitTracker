package com.example.habittracker.presentation.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habittracker.presentation.vm.HabitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListScreen(
    viewModel: HabitViewModel = hiltViewModel(),
    onHabitClick: (String) -> Unit = {}
) {
}