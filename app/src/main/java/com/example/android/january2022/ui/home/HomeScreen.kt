package com.example.android.january2022.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.january2022.db.entities.Session
import com.example.android.january2022.utils.Event
import com.example.android.january2022.utils.UiEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val sessions by viewModel.sessionList.observeAsState(listOf())
    val composableScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val colors = MaterialTheme.colors

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit // do nothing
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = { BottomAppBar() {

        }},
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(HomeEvent.OnAddSessionClick) },
                shape = RoundedCornerShape(35),
                containerColor = colors.primary,
            ) {
                Icon(
                    Icons.Filled.Add,
                    "Start new session",
                    tint = colors.onPrimary
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        SessionCardList(sessions = sessions, viewModel)
    }
}

@Composable
fun SessionCardList(
    sessions: List<Session>,
    viewModel: HomeViewModel
) {
    val sessionContent = viewModel.sessionExerciseList.observeAsState(listOf())
    val sets = viewModel.sets.observeAsState(listOf())
    LazyColumn {
        items(items = sessions) { session ->
            SessionCard(
                session,
                sessionContent.value,
                sets.value,
                viewModel,
                viewModel::onEvent
            )
        }
        item { Spacer(Modifier.height(86.dp)) }
    }
}


