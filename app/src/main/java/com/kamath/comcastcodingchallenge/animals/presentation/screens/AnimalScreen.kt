package com.kamath.comcastcodingchallenge.animals.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kamath.comcastcodingchallenge.animals.presentation.AnimalScreenEvent
import com.kamath.comcastcodingchallenge.animals.presentation.AnimalScreenState
import com.kamath.comcastcodingchallenge.animals.presentation.AnimalScreenViewModel
import com.kamath.comcastcodingchallenge.animals.presentation.screens.components.AnimalListContent
import com.kamath.comcastcodingchallenge.animals.presentation.screens.components.SearchBar

@Composable
fun AnimalScreen(
    viewModel: AnimalScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        AnimalScreenContent(
            uiState,
            onEvent = viewModel::onEvent,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun AnimalScreenContent(
    uiState: AnimalScreenState,
    onEvent: (AnimalScreenEvent) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SearchBar(
            query = uiState.searchQuery,
            onQueryChange = { onEvent(AnimalScreenEvent.OnAnimalSearch(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.testTag("progress")
                )
            } else {
                AnimalListContent(
                    animals = uiState.animals,
                    horizontalAlignment = Alignment.CenterHorizontally,
                )
            }
        }
    }
}

