package com.example.android.january2022.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.utils.UiEvent
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val sessions by viewModel.sessionList.observeAsState(listOf())
    val colors = MaterialTheme.colorScheme
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                is UiEvent.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.actionLabel,
                        withDismissAction = true
                    )
                    if(result == SnackbarResult.ActionPerformed) {
                        if(event.action != null) viewModel.onEvent(event.action)
                    }
                }
                else -> Unit // do nothing
            }
        }
    }
    Scaffold(
        modifier = Modifier.padding(bottom = 60.dp),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(HomeEvent.OnAddSessionClick) },
                shape = RoundedCornerShape(35),
                containerColor = colors.primary,
            ) {
                Icon(Icons.Filled.Add, "Start new session", tint = colors.onPrimary)
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { padding ->
        SessionCardList(Modifier.padding(padding).statusBarsPadding(), sessions, viewModel)
    }
}

@Composable
fun SessionCardList(
    modifier: Modifier = Modifier,
    sessions: List<Session>,
    viewModel: HomeViewModel
) {
    val sessionContent by viewModel.sessionExerciseList.observeAsState(listOf())
    val sets by viewModel.sets.observeAsState(listOf())
    val selectedSession by derivedStateOf { viewModel.selectedSession }
    LazyColumn(modifier = modifier) {
        items(sessions, key = { it.sessionId }) { session ->
            SessionCard(
                session = session,
                sessionContent = sessionContent.filter { it.sessionExercise.parentSessionId == session.sessionId },
                sets = sets,
                selected = selectedSession,
                viewModel = viewModel,
                onEvent = viewModel::onEvent
            )
        }
    }
}



